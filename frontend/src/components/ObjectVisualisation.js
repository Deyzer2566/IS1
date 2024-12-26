import React, { useState, useEffect } from 'react';
import { Stage, Layer, Image } from 'react-konva';
import { getFlats, getRights } from '../services/api';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';

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

  useEffect(() => {
    fetchFlats();
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
            window.innerWidth-maxArea);
        setKX(k);
        setBX(b);
        [k,b] = normalize(
            Math.min(...response.data.map(flat=>flat.coordinates.y)),
            Math.max(...response.data.map(flat=>flat.coordinates.y)),
            0,
            window.innerHeight-maxArea);
        setKY(k);
        setBY(b);
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
      <Stage width={window.innerWidth} height={window.innerHeight} >
        <Layer>
          {imgReady && flats.map(flat => (
            <Image
              key={flat.id}
              x={flat.coordinates.x * k_x + b_x}
              y={flat.coordinates.y * k_y + b_y}
              width={flat.area * k_area + b_area}
              height={flat.area * k_area + b_area}
              image={img}
              onClick={() => handleClick(flat)}
            />
            ))}
        </Layer>
      </Stage>
      {selectedFlat && (
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
        </div>
      )}
    </div>
  );
};

export default ObjectVisualization;
