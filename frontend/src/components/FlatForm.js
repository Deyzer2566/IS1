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
  };

  const { user } = useAuth();
  if (!user)
    return (<Navigate to="/auth" />);

  return (
    <form onSubmit={handleSubmit}>
      <div>
        <label>Name:</label>
        <input type="text" name="name" value={flat.name} onChange={handleChange} required />
      </div>
      <div>
        <label>Area:</label>
        <input type="number" name="area" value={flat.area} onChange={handleChange} required />
      </div>
      <div>
        <label>Price:</label>
        <input type="number" name="price" value={flat.price} onChange={handleChange} required />
      </div>
      <div>
        <label>Balcony:</label>
        <input type="checkbox" name="balcony" checked={flat.balcony} onChange={(e) => setFlat({ ...flat, balcony: e.target.checked })} />
      </div>
      <div>
        <label>Time to Metro on Foot:</label>
        <input type="number" name="timeToMetroOnFoot" value={flat.timeToMetroOnFoot} onChange={handleChange} required />
      </div>
      <div>
        <label>Number of Rooms:</label>
        <input type="number" name="numberOfRooms" value={flat.numberOfRooms} onChange={handleChange} required />
      </div>
      <div>
        <label>Time to Metro by Transport:</label>
        <input type="number" name="timeToMetroByTransport" value={flat.timeToMetroByTransport} onChange={handleChange} required />
      </div>
      <div>
        <label>Furnish:</label>
        <select name="furnish" value={flat.furnish} onChange={handleChange} required>
          <option value="">Select Furnish</option>
          <option value="DESIGNER">Designer</option>
          <option value="NONE">None</option>
          <option value="FINE">Fine</option>
          <option value="BAD">Bad</option>
          <option value="LITTLE">Little</option>
        </select>
      </div>
      <div>
        <label>View:</label>
        <select name="view" value={flat.view} onChange={handleChange} required>
          <option value="">Select View</option>
          <option value="STREET">Street</option>
          <option value="BAD">Bad</option>
          <option value="TERRIBLE">Terrible</option>
        </select>
      </div>
      <div>
        <label>House:</label>
        <select name="house" value={flat.house ? flat.house.id : ''} onChange={handleSelectHouse}>
          <option value="">Select House</option>
          {houses.map(house => (
            <option key={house.id} value={house.id}>{house.name}</option>
          ))}
        </select>
        <button type="button" onClick={() => setShowNewHouseForm(true)}>Create New House</button>
        <button type="button" onClick={handleNoHouse}>No House</button>
      </div>
      {showNewHouseForm && (
        <div>
          <h3>Create New House</h3>
          <div>
            <label>Name:</label>
            <input type="text" name="name" value={flat.house ? flat.house.name : ''} onChange={handleHouseChange} required />
          </div>
          <div>
            <label>Year:</label>
            <input type="number" name="year" value={flat.house ? flat.house.year : ''} onChange={handleHouseChange} required />
          </div>
          <div>
            <label>Number of Floors:</label>
            <input type="number" name="numberOfFloors" value={flat.house ? flat.house.numberOfFloors : ''} onChange={handleHouseChange} required />
          </div>
          <div>
            <label>Number of Flats on Floor:</label>
            <input type="number" name="numberOfFlatsOnFloor" value={flat.house ? flat.house.numberOfFlatsOnFloor : ''} onChange={handleHouseChange} required />
          </div>
          <div>
            <label>Number of Lifts:</label>
            <input type="number" name="numberOfLifts" value={flat.house ? flat.house.numberOfLifts : ''} onChange={handleHouseChange} required />
          </div>
        </div>
      )}
      <div>
        <label>Coordinates X:</label>
        <input type="number" name="coordinates.x" value={flat.coordinates.x} onChange={handleChange} required />
      </div>
      <div>
        <label>Coordinates Y:</label>
        <input type="number" name="coordinates.y" value={flat.coordinates.y} onChange={handleChange} required min="-670"/>
      </div>
      <button type="submit">{id ? 'Update' : 'Create'}</button>
      {message && <p>{message}</p>}
    </form>
  );
};

export default FlatForm;
