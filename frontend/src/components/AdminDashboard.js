import React, { useState, useEffect } from 'react';
import { getApplications, approveApplication, rejectApplication } from '../services/api';

const AdminDashboard = () => {
  const [applications, setApplications] = useState([]);

  useEffect(() => {
    getApplications()
      .then(response => setApplications(response.data['applications']))
      .catch(error => console.error(error));
  }, []);

  const handleApprove = (applicationId) => {
    approveApplication(applicationId)
      .then(response => {
        setApplications(applications.filter(app => app.id !== applicationId));
      })
      .catch(error => console.error('Error approving application:', error));
  };

  const handleReject = (applicationId) => {
    rejectApplication(applicationId)
      .then(response => {
        setApplications(applications.filter(app => app.id !== applicationId));
      })
      .catch(error => console.error('Error rejecting application:', error));
  };

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
