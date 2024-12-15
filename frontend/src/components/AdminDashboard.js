import React, { useState, useEffect } from 'react';
import { getApplications } from '../services/api';

const AdminDashboard = () => {
  const [applications, setApplications] = useState([]);

  useEffect(() => {
    getApplications()
      .then(response => setApplications(response.data['applications']))
      .catch(error => console.error(error));
  }, []);

  return (
    <div>
      <h1>Admin Dashboard</h1>
      <h2>Applications for Admin Role</h2>
      <table>
        <thead>
          <tr>
            <th>Application ID</th>
            <th>Username</th>
            <th>Actions</th>
          </tr>
        </thead>
        
        <tbody>
          {applications.map(app => (
            <tr>
              <td>{app.id}</td>
              <td>{app.userName}</td>
              <td>
                <button>Approve</button>
                <button>Reject</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default AdminDashboard;
