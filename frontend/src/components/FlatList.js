import React, { useState, useEffect } from 'react';
import { getFlats, deleteFlat } from '../services/api';
import { Link, useNavigate } from 'react-router-dom';

const FlatList = () => {
  const [flats, setFlats] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [message, setMessage] = useState('');
  const [filter, setFilter] = useState('');
  const [filterBy, setFilterBy] = useState('');
  const [sortField, setSortField] = useState('');
  const [sortOrder, setSortOrder] = useState('false');
  const navigate = useNavigate();

  useEffect(() => {
    fetchFlats();
    // const interval = setInterval(fetchFlats, 1000); // Обновление каждую секунду
    // return () => clearInterval(interval); // Очистка интервала при размонтировании компонента
  }, [currentPage, filter, sortField, sortOrder]);

  const fetchFlats = () => {
    getFlats(currentPage, filterBy, filter, sortField, sortOrder)
      .then(response => {
        setFlats(response.data || []);
        setTotalPages(response.data.totalPages);
      })
      .catch(error => console.error(error));
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const handleDelete = (id) => {
    deleteFlat(id)
      .then(response => {
        setMessage(response.data.message);
        fetchFlats();
      })
      .catch(error => {
        setMessage(error.response.data.message);
      });
  };

  const handleAddFlat = () => {
    navigate('/flats/new');
  };

  const handleFilterChange = (e) => {
    setFilter(e.target.value);
  };

  const handleFilterByChange = (e) => {
    setFilterBy(e.target.value);
  };

  const handleSortChange = (e) => {
    setSortField(e.target.value);
  };

  const handleSortOrderChange = (e) => {
    setSortOrder(e.target.value);
  };

  const itemsPerPage = 10; // Количество квартир на странице

  return (
    <div>
      <h1>Flats</h1>
      <button onClick={handleAddFlat}>Add Flat</button>
      <div>
        <label>Filter by</label>
        <select value={filterBy} onChange={handleFilterByChange}>
          <option value="">Select</option>
          <option value="name">Name</option>
          <option value="view">View</option>
          <option value="furnish">Furnish</option>
        </select>
        <input type="text" value={filter} onChange={handleFilterChange} placeholder="Filter value" />
      </div>
      <div>
        <label>Sort by:</label>
        <select value={sortField} onChange={handleSortChange}>
          <option value="">Select</option>
          <option value="name">Name</option>
          <option value="view">View</option>
          <option value="furnish">Furnish</option>
        </select>
        <select value={sortOrder} onChange={handleSortOrderChange}>
          <option value="false">Ascending</option>
          <option value="true">Descending</option>
        </select>
      </div>
      {flats.length > 0 ? (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Area</th>
              <th>Price</th>
              <th>Balcony</th>
              <th>Time to Metro on Foot</th>
              <th>Number of Rooms</th>
              <th>Time to Metro by Transport</th>
              <th>Furnish</th>
              <th>View</th>
              <th>Coordinates X</th>
              <th>Coordinates Y</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {flats.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage).map(flat => (
              <tr key={flat.id}>
                <td>{flat.id}</td>
                <td>{flat.name}</td>
                <td>{flat.area}</td>
                <td>{flat.price}</td>
                <td>{flat.balcony ? 'Yes' : 'No'}</td>
                <td>{flat.timeToMetroOnFoot}</td>
                <td>{flat.numberOfRooms}</td>
                <td>{flat.timeToMetroByTransport}</td>
                <td>{flat.furnish}</td>
                <td>{flat.view}</td>
                <td>{flat.coordinates.x}</td>
                <td>{flat.coordinates.y}</td>
                <td>
                  <Link to={`/flats/${flat.id}/edit`}>Edit</Link>
                  <button onClick={() => handleDelete(flat.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>No flats available. Please add a new flat.</p>
      )}
      <div>
        {Array.from({ length: totalPages }, (_, i) => (
          <button key={i} onClick={() => handlePageChange(i + 1)}>
            {i + 1}
          </button>
        ))}
      </div>
      {message && <p>{message}</p>}
    </div>
  );
};

export default FlatList;
