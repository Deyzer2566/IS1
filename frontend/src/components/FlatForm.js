import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Navigate } from 'react-router-dom';
import { getFlat, createFlat, updateFlat, getHouses } from '../services/api';
import { useAuth } from '../context/AuthContext';

const FlatForm = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [flat, setFlat] = useState({
    name: '',
    area: '',
    price: '',
    balcony: false,
    timeToMetroOnFoot: '',
    numberOfRooms: '',
    timeToMetroByTransport: '',
    furnish: '',
    view: '',
    house: null, // Используем null для house, если не выбрано
    coordinates: {
      x: '',
      y: ''
    }
  });
  const [houses, setHouses] = useState([]);
  const [showNewHouseForm, setShowNewHouseForm] = useState(false);
  const [message, setMessage] = useState('');
  const {logout} = useAuth();

  useEffect(() => {
    if (id) {
      getFlat(id)
        .then(response => setFlat(response.data))
        .catch(error => console.error(error));
    }
    getHouses()
      .then(response => setHouses(response.data))
      .catch(error => console.error(error));

    // Восстанавливаем данные из localStorage, если они есть
    const savedFlat = JSON.parse(localStorage.getItem('flatFormData'));
    if (savedFlat) {
      setFlat(savedFlat);
    }
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    if (name.includes('coordinates')) {
      const [field, subField] = name.split('.');
      setFlat(prevState => ({
        ...prevState,
        [field]: {
          ...prevState[field],
          [subField]: value
        }
      }));
    } else {
      setFlat(prevState => ({
        ...prevState,
        [name]: value
      }));
    }
  };

  const handleHouseChange = (e) => {
    const { name, value } = e.target;
    setFlat(prevState => ({
      ...prevState,
      house: {
        ...prevState.house,
        [name]: value
      }
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if(typeof(flat.area) == "string")
      flat.area.replace(',','.');
    if(typeof(flat.timeToMetroByTransport) == "string")
      flat.timeToMetroByTransport.replace(',','.');
    const flatToSend = { ...flat };
    if (!flat.house) {
      delete flatToSend.house;
    }
    if (id) {
      updateFlat(id, flatToSend)
        .then(response => {
          setMessage(response.data.message);
          navigate('/flats');
          localStorage.removeItem('flatFormData'); // Удаляем данные из localStorage после успешного запроса
        })
        .catch(error => {
          setMessage(error.response.data.message);
          if (error.response.status === 401) {
            localStorage.setItem('flatFormData', JSON.stringify(flatToSend)); // Сохраняем данные в localStorage перед перенаправлением
            logout();
            navigate('/auth');
          }
        });
    } else {
      createFlat(flatToSend)
        .then(response => {
          setMessage(response.data.message);
          navigate('/flats');
          localStorage.removeItem('flatFormData'); // Удаляем данные из localStorage после успешного запроса
        })
        .catch(error => {
          setMessage(error.response.data.message);
          if (error.response.status === 401) {
            localStorage.setItem('flatFormData', JSON.stringify(flatToSend)); // Сохраняем данные в localStorage перед перенаправлением
            logout();
            navigate('/auth');
          }
        });
    }
  };

  const handleSelectHouse = (e) => {
    const selectedHouse = houses.find(house => house.id === parseInt(e.target.value));
    setFlat(prevState => ({
      ...prevState,
      house: selectedHouse
    }));
  };

  const handleNoHouse = () => {
    setFlat(prevState => ({
      ...prevState,
      house: null
    }));
    setShowNewHouseForm(false);
  };

  const { user } = useAuth();
  if (!user)
    return (<Navigate to="/auth" />);

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>Название:</label>
        <input type="text" name="name" value={flat.name} onChange={handleChange} required />
      </div>
      <div>
        <label>Площадь:</label>
        <input type="number" name="area" value={flat.area} onChange={handleChange} required min="1" step="0.001"/>
      </div>
      <div>
        <label>Цена:</label>
        <input type="number" name="price" value={flat.price} onChange={handleChange} required min="1" step="1"/>
      </div>
      <div>
        <label>Есть балкон:</label>
        <input type="checkbox" name="balcony" checked={flat.balcony} onChange={(e) => setFlat({ ...flat, balcony: e.target.checked })} />
      </div>
      <div>
        <label>Время до метро пешком:</label>
        <input type="number" name="timeToMetroOnFoot" value={flat.timeToMetroOnFoot} onChange={handleChange} required min="1" step="1"/>
      </div>
      <div>
        <label>Количество комнат:</label>
        <input type="number" name="numberOfRooms" value={flat.numberOfRooms} onChange={handleChange} required min="0" step="1"/>
      </div>
      <div>
        <label>Время до метро на транспорте:</label>
        <input type="number" name="timeToMetroByTransport" value={flat.timeToMetroByTransport} onChange={handleChange} required min="1" step="0.001"/>
      </div>
      <div>
        <label>Мебель:</label>
        <select name="furnish" value={flat.furnish} onChange={handleChange} required>
          <option value="">Ремонт</option>
          <option value="DESIGNER">Дизайнерский</option>
          <option value="NONE">Нет</option>
          <option value="FINE">Прекрасный</option>
          <option value="BAD">Срач</option>
          <option value="LITTLE">Бабушкин ремонт</option>
        </select>
      </div>
      <div>
        <label>Вид из окна:</label>
        <select name="view" value={flat.view} onChange={handleChange} required>
          <option value="">Выберите элемент из списка</option>
          <option value="STREET">Улица</option>
          <option value="BAD">Плохой</option>
          <option value="TERRIBLE">К&Б</option>
        </select>
      </div>
      <div>
        <label>Дом:</label>
        <select name="house" value={flat.house ? flat.house.id : ''} onChange={handleSelectHouse}>
          <option value="">Выберите дом</option>
          {houses.map(house => (
            <option key={house.id} value={house.id}>{house.name}</option>
          ))}
        </select>
        <button type="button" onClick={() => setShowNewHouseForm(true)}>Добавить дом</button>
        <button type="button" onClick={handleNoHouse}>Без дома</button>
      </div>
      {(showNewHouseForm || (flat && flat.house)) && (
        <div>
          {!id && <h3>Заполнение информации о доме</h3>}
          {id && <h3>Информация о доме</h3>}
          <div>
            <label>Название:</label>
            <input type="text" name="name" value={flat.house ? flat.house.name : ''} onChange={handleHouseChange} required />
          </div>
          <div>
            <label>Год постройки:</label>
            <input type="number" name="year" value={flat.house ? flat.house.year : ''} onChange={handleHouseChange} required min="1900" step="1"/>
          </div>
          <div>
            <label>Количество этажей:</label>
            <input type="number" name="numberOfFloors" value={flat.house ? flat.house.numberOfFloors : ''} onChange={handleHouseChange} required min="1" step="1"/>
          </div>
          <div>
            <label>Количество квартир на этаже:</label>
            <input type="number" name="numberOfFlatsOnFloor" value={flat.house ? flat.house.numberOfFlatsOnFloor : ''} onChange={handleHouseChange} required min="1" step="1"/>
          </div>
          <div>
            <label>Количество лифтов:</label>
            <input type="number" name="numberOfLifts" value={flat.house ? flat.house.numberOfLifts : ''} onChange={handleHouseChange} required min="1" step="1"/>
          </div>
        </div>
      )}
      <div>
        <label>Координата X:</label>
        <input type="number" name="coordinates.x" value={flat.coordinates.x} onChange={handleChange} required min="-10000" step="1"/>
      </div>
      <div>
        <label>Координата Y:</label>
        <input type="number" name="coordinates.y" value={flat.coordinates.y} onChange={handleChange} required min="-670" step="1"/>
      </div>
      <button type="submit">{id ? 'Обновить объявление' : 'Добавить объявление'}</button>
      {message && <p>{message}</p>}
    </form>
  );
};

export default FlatForm;
