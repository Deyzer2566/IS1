import React, { useState, useEffect } from 'react';
import { getExportHistory, exportFlats } from '../services/api';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const ExportForm = () => {
  const [exports, setExports] = useState([]);
  const [message, setMessage] = useState('');
  const navigate = useNavigate();
  const {logout} = useAuth();

  const fetchExports = () => {
    getExportHistory()
      .then(response => {
        setExports(response.data || []);
      })
      .catch(error => {console.error(error); if(error.status == 401) {logout(); navigate("/auth");}});
  };

  const handleSubmit = (e) => {
    setMessage('Ожидание...');
    e.preventDefault();
    const formData = new FormData(e.target);
    exportFlats(formData).then(()=>setMessage('Гуд')).catch((err)=>{
      if(err.status == 400) {
        if(err.response.data == 'Ошибка обработки файла: null')
          setMessage('Ошибка обработки файла: Координаты должны быть уникальными!')
        else setMessage(err.response.data);
      }
      else if (err.status == 500)
        setMessage('Ошибка на стороне сервера')
      else if (err.status == 401) {
        logout();
        navigate("/auth");
      }
    }).finally(() => {
      fetchExports();
      e.target.reset();
    });
  }

  useEffect(fetchExports, []);

  return (
    <div>
      <h1>Экспорт файлов</h1>
      <form onSubmit={handleSubmit}>
        <input type="file" name="file" required />
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
                {export_.exportStatus == "SUCCESS" && <td><a href={`http://localhost:8080/api/management/export/${export_.id}.yaml`} download>{export_.id}</a></td>}
                {export_.exportStatus == "FAIL" && <td>{export_.id}</td>}
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
