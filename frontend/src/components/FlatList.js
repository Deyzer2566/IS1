import React, { useEffect, useState } from 'react';
import { getFlats, deleteFlat } from '../services/api';
import { Link, useNavigate } from 'react-router-dom';

const FlatList = () => {
  const [flats, setFlats] = useState([]);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const navigate = useNavigate();

  useEffect(() => {
    getFlats()
      .then(response => {
        setFlats(response.data || []); // Убедимся, что flats всегда массив
        setTotalPages(1);
      })
      .catch(error => console.error(error));
  }, []);

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const handleDelete = (id) => {
    deleteFlat(id)
      .then(() => {
        setFlats(flats.filter(flat => flat.id !== id));
      })
      .catch(error => console.error(error));
  };

  const handleAddFlat = () => {
    navigate('/flats/new');
  };

  return (
    <div>
      <h1>Flats</h1>
      <button onClick={handleAddFlat}>Add Flat</button>
      {flats.length > 0 ? (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Area</th>
              <th>Price</th>
              <th>Balcony</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {flats.map(flat => (
              <tr key={flat.id}>
                <td>{flat.id}</td>
                <td>{flat.name}</td>
                <td>{flat.area}</td>
                <td>{flat.price}</td>
                <td>{flat.balcony ? 'Yes' : 'No'}</td>
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
    </div>
  );
};

export default FlatList;
