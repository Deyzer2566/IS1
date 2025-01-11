import React, { useState, useEffect } from 'react';
import { getFlats, deleteFlat, getRights } from '../services/api';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

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
  const {user, logout} = useAuth();
  const [canChange, setCanChange] = useState([]);

  useEffect(() => {
    fetchFlats();
    const interval = setInterval(fetchFlats, 1000); // Обновление каждую секунду
    return () => clearInterval(interval); // Очистка интервала при размонтировании компонента
  }, [currentPage, filter, sortField, sortOrder]);

  const fetchFlats = () => {
    getFlats(currentPage, filterBy, filter, sortField, sortOrder)
      .then(response => {
        setFlats(response.data || []);
        setTotalPages(Math.ceil(response.data.length / itemsPerPage));
        if (currentPage > totalPages)
          setCurrentPage(1);
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
        setMessage("Нет прав на удаление данного объекта");
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

  const itemsPerPage = 6; // Количество квартир на странице

  return (
    <div>
      <h1>Flats</h1>
      <button onClick={handleAddFlat}>Добавить квартирку на Циан 2.0</button>
      <button onClick={()=>navigate("/smo")}>Специальные операции</button>
      <div>
        <label>Filter by</label>
        <select value={filterBy} onChange={handleFilterByChange}>
          <option value="">Select</option>
          <option value="name">Имя</option>
          <option value="view">Вид из окон</option>
          <option value="furnish">Мебель</option>
        </select>
        <input type="text" value={filter} onChange={handleFilterChange} placeholder="Filter value" />
      </div>
      <div>
        <label>Sort by:</label>
        <select value={sortField} onChange={handleSortChange}>
          <option value="">Select</option>
          <option value="name">Имя</option>
          <option value="view">Вид из окон</option>
          <option value="furnish">Мебель</option>
        </select>
        <select value={sortOrder} onChange={handleSortOrderChange}>
          <option value="false">По возрастанию</option>
          <option value="true">По убыванию</option>
        </select>
      </div>
      {flats.length > 0 ? (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Название</th>
              <th>Площать</th>
              <th>Цена</th>
              <th>Наличие балкона</th>
              <th>Время до метро пешком</th>
              <th>Количество комнат</th>
              <th>Время до метро на транспорте</th>
              <th>Мебель</th>
              <th>Вид из окон</th>
              <th>Координата X</th>
              <th>Координата Y</th>
              <th>Действия</th>
            </tr>
          </thead>
          <tbody>
            {flats.slice((currentPage - 1) * itemsPerPage, currentPage * itemsPerPage).map(flat => (
              <tr key={flat.id}>
                <td>{flat.id}</td>
                <td>{flat.name}</td>
                <td>{flat.area}</td>
                <td>{flat.price}</td>
                <td>{flat.balcony ? 'Есть' : 'Нет'}</td>
                <td>{flat.timeToMetroOnFoot}</td>
                <td>{flat.numberOfRooms}</td>
                <td>{flat.timeToMetroByTransport}</td>
                <td>{flat.furnish}</td>
                <td>{flat.view}</td>
                <td>{flat.coordinates.x}</td>
                <td>{flat.coordinates.y}</td>
                <td>
                  {canChange.includes(flat.id) && (<button onClick={()=>navigate(`/flats/${flat.id}/edit`)}>Редактировать</button>)}
                  {canChange.includes(flat.id) && (<button onClick={() => handleDelete(flat.id)}>Удалить</button>)}
                  {!canChange.includes(flat.id) && (<p>Пока что не ваш дом</p>)}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>Список квартир пуст. Станьте первым пользователем Циан 2.0!</p>
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
