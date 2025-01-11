import React, { useState, useEffect } from 'react';
import { Stage, Layer, Image, Rect } from 'react-konva';
import { getFlats, getRights, getOwner } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import Konva from 'konva';

const ObjectVisualization = () => {
  const [flats, setFlats] = useState([]);
  const [selectedFlat, setSelectedFlat] = useState(null);

  const minArea = 20;
  const maxArea = 80;
  const [k_area, setKArea] = useState(0);
  const [b_area, setBArea] = useState(0);
  const [k_x, setKX] = useState(0);
  const [b_x, setBX] = useState(0);
  const [k_y, setKY] = useState(0);
  const [b_y, setBY] = useState(0);
  const img = new window.Image();
  const [imgReady, setImgReady] = useState(false);
  img.onload = () => {setImgReady(true)};
  img.onerror = () => {console.log('cant load img house');}
  img.src = '/house.png';
  const [canChange, setCanChange] = useState([]);
  const navigate = useNavigate();
  const {user, logout} = useAuth();

  const [owners, setOwners] = useState({});
  
  const getOwners = (flats) => {
    setOwners({});
    return Promise.all(flats.map(flat => getOwner(flat.id)));
  }

  useEffect(() => {
    fetchFlats();
    const interval = setInterval(fetchFlats, 10000); // Обновление каждую секунду
    return () => clearInterval(interval); // Очистка интервала при размонтировании компонента
  }, []);

  const normalize = (min, max, fmin, fmax) => {
    if(max!=min) {
        let k = (fmax-fmin)/(max-min);
        let b = fmin - min*k;
        return [k,b];
    } else {
        return [0, (fmax+fmin)/2]
    }
  }

  const fetchFlats = () => {
    getFlats()
      .then(response => {
        setFlats(response.data || []);
        let [k,b] = normalize(
            Math.min(...response.data.map(flat=>flat.area)),
            Math.max(...response.data.map(flat=>flat.area)),
            minArea,
            maxArea);
        setKArea(k);
        setBArea(b);
        [k,b] = normalize(
            Math.min(...response.data.map(flat=>flat.coordinates.x)),
            Math.max(...response.data.map(flat=>flat.coordinates.x)),
            0,
            window.innerWidth-maxArea-100);
        setKX(k);
        setBX(b);
        [k,b] = normalize(
            Math.min(...response.data.map(flat=>flat.coordinates.y)),
            Math.max(...response.data.map(flat=>flat.coordinates.y)),
            0,
            window.innerHeight-maxArea-100);
        setKY(k);
        setBY(b);
        getOwners(response.data).then(
          responses=>{
            setOwners(
              responses.map(
                r=>{
                  return {[Number(r.request.responseURL.match('/api/flat/(\\d+)/owner')[1])]: r.data}
                }
              ).reduce((a,v)=>{return {...a, ...v}})
            )
          }
        )
      })
      .catch(error => console.error(error));
      getRights()
            .then(response => {
              setCanChange(response.data);
            })
            .catch(response => {
              if(user) {
                logout();
              }
            });
  };

  const handleClick = (flat) => {
    setSelectedFlat(flat);
  };

  const getColor = (userId) => {
    // Генерация случайного цвета для каждого пользователя
    const colors = ['red', 'blue', 'green', 'yellow', 'purple', 'orange'];
    return colors[userId % colors.length];
  };

  return (
    <div>
      <Stage width={window.innerWidth-100} height={window.innerHeight-100} >
        <Layer>
          {imgReady && flats.map(flat => {
            return owners[flat.id] && (
            <Image
              key={flat.id}
              x={flat.coordinates.x * k_x + b_x}
              y={flat.coordinates.y * k_y + b_y}
              width={flat.area * k_area + b_area}
              height={flat.area * k_area + b_area}
              image={img}
              onClick={() => handleClick(flat)}
              fill={getColor(owners[flat.id].charCodeAt(0))}
            />
            )})}
        </Layer>
      </Stage>
      {selectedFlat && flats.map(flat=>flat.id).includes(selectedFlat.id) && (
        <div style={{ position: 'absolute', top: 10, left: 10, background: 'white', padding: 10, border: '1px solid black' }}>
          <h3>Информация о квартирке</h3>
          <p>ID: {selectedFlat.id}</p>
          <p>Название: {selectedFlat.name}</p>
          <p>Площадь: {selectedFlat.area}</p>
          <p>Цена: {selectedFlat.price}</p>
          <p>Наличие балкона: {selectedFlat.balcony ? 'Есть' : 'Нет'}</p>
          <p>Время до метро пешком: {selectedFlat.timeToMetroOnFoot}</p>
          <p>Количество комнат: {selectedFlat.numberOfRooms}</p>
          <p>Время до метро на транспорте: {selectedFlat.timeToMetroByTransport}</p>
          <p>Мебель: {selectedFlat.furnish}</p>
          <p>Вид: {selectedFlat.view}</p>
          <p>Координаты: ({selectedFlat.coordinates.x}, {selectedFlat.coordinates.y})</p>
          {canChange && canChange.includes(selectedFlat.id) && (<button onClick={()=>navigate(`/flats/${selectedFlat.id}/edit`)}>Нашли ошибку?</button>)}
          <button onClick={()=>{setSelectedFlat(null)}}>Закрыть объявление</button>
        </div>
      )}
    </div>
  );
};

export default ObjectVisualization;
