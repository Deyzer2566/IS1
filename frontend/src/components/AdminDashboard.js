import React, { useState, useEffect } from 'react';
import { getApplications, approveApplication, rejectApplication } from '../services/api';
import { useNavigate } from 'react-router-dom';
const AdminDashboard = () => {
  const [applications, setApplications] = useState([]);
  const navigate = useNavigate();

  const fetchApplications = () => {
    getApplications()
    .then(response => setApplications(response.data['applications']))
    .catch(error => console.error(error));
  }

  useEffect(fetchApplications, []);

  const handleApprove = (applicationId) => {
    approveApplication(applicationId)
      .then(fetchApplications)
      .catch(error => console.error('Error approving application:', error));
  };

  const handleReject = (applicationId) => {
    rejectApplication(applicationId)
      .then(fetchApplications)
      .catch(error => console.error('Error rejecting application:', error));
  };

  return (
    <div>
      <h1>Админ панель</h1>
      <h2>Заявки на получение админки</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Имя пользователя</th>
            <th>Действия</th>
          </tr>
        </thead>
        
        <tbody>
          {applications.map(app => (
            <tr>
              <td>{app.id}</td>
              <td>{app.userName}</td>
              <td>
                <button onClick={() => handleApprove(app.id)}>Approve</button>
                <button onClick={() => handleReject(app.id)}>Reject</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminDashboard;
