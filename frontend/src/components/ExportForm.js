import React, { useState, useEffect } from 'react';
import { getExportHistory, exportFlats } from '../services/api';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const ExportForm = () => {
  const [exports, setExports] = useState([]);
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState('');

  const fetchExports = () => {
    getExportHistory()
      .then(response => {
        setExports(response.data || []);
      })
      .catch(error => console.error(error));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('file', file);
    exportFlats(formData).then(()=>setMessage('Гуд')).catch(()=>setMessage('Что-то не так'));
  }

  const handleFileChange = (e) => {
    const value = e.target.files[0];
    setFile(value);
  };

  useEffect(fetchExports, []);

  return (
    <div>
      <h1>Экспорт файлов</h1>
      <form onSubmit={handleSubmit}>
        <input type="file" name="file" onChange={handleFileChange} required />
        <button type="submit">Загрузить файл</button>
      </form>
      {message && <p>{message}</p>}
      {exports.length > 0 ? (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Пользователь</th>
              <th>Статус</th>
              <th>Добавлено квартир</th>
            </tr>
          </thead>
          <tbody>
            {exports.map(export_ => (
              <tr key={export_.id}>
                <td>{export_.id}</td>
                <td>{export_.userz.login}</td>
                <td>{export_.exportStatus}</td>
                <td>{export_.flatsAdded ? export_.flatsAdded : "-"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>Эм...</p>
      )}
    </div>
  );
};

export default ExportForm;
