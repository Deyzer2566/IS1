import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getFlat, createFlat, updateFlat, getHouses } from '../services/api';

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
    house: {
      name: '',
      year: '',
      numberOfFloors: '',
      numberOfFlatsOnFloor: '',
      numberOfLifts: ''
    },
    coordinates: {
      x: '',
      y: ''
    }
  });
  const [houses, setHouses] = useState([]);
  const [showNewHouseForm, setShowNewHouseForm] = useState(false);

  useEffect(() => {
    if (id) {
      getFlat(id)
        .then(response => setFlat(response.data))
        .catch(error => console.error(error));
    }
    getHouses()
      .then(response => setHouses(response.data))
      .catch(error => console.error(error));
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
    if (id) {
      updateFlat(id, flat)
        .then(() => navigate('/flats'))
        .catch(error => console.error(error));
    } else {
      createFlat(flat)
        .then(() => navigate('/flats'))
        .catch(error => console.error(error));
    }
  };

  const handleSelectHouse = (e) => {
    const selectedHouse = houses.find(house => house.id === parseInt(e.target.value));
    setFlat(prevState => ({
      ...prevState,
      house: selectedHouse
    }));
  };

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
        <select name="house" value={flat.house.id || ''} onChange={handleSelectHouse}>
          <option value="">Select House</option>
          {houses.map(house => (
            <option key={house.id} value={house.id}>{house.name}</option>
          ))}
        </select>
        <button type="button" onClick={() => setShowNewHouseForm(true)}>Create New House</button>
      </div>
      {showNewHouseForm && (
        <div>
          <h3>Create New House</h3>
          <div>
            <label>Name:</label>
            <input type="text" name="name" value={flat.house.name} onChange={handleHouseChange} required />
          </div>
          <div>
            <label>Year:</label>
            <input type="number" name="year" value={flat.house.year} onChange={handleHouseChange} required />
          </div>
          <div>
            <label>Number of Floors:</label>
            <input type="number" name="numberOfFloors" value={flat.house.numberOfFloors} onChange={handleHouseChange} required />
          </div>
          <div>
            <label>Number of Flats on Floor:</label>
            <input type="number" name="numberOfFlatsOnFloor" value={flat.house.numberOfFlatsOnFloor} onChange={handleHouseChange} required />
          </div>
          <div>
            <label>Number of Lifts:</label>
            <input type="number" name="numberOfLifts" value={flat.house.numberOfLifts} onChange={handleHouseChange} required />
          </div>
        </div>
      )}
      <div>
        <label>Coordinates X:</label>
        <input type="number" name="coordinates.x" value={flat.coordinates.x} onChange={handleChange} required />
      </div>
      <div>
        <label>Coordinates Y:</label>
        <input type="number" name="coordinates.y" value={flat.coordinates.y} onChange={handleChange} required />
      </div>
      <button type="submit">{id ? 'Update' : 'Create'}</button>
    </form>
  );
};

export default FlatForm;
