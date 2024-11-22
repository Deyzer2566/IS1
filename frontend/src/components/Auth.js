import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { auth, register } from '../services/api';

const Auth = () => {
  const [login, setlogin] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = () => {
    auth({ login, password })
      .then(response => {
        localStorage.setItem('token', response.data.token);
        navigate('/flats');
      })
      .catch(error => console.error(error));
  };

  const handleRegister = () => {
    register({ login, password })
      .then(() => handleLogin())
      .catch(error => console.error(error));
  };

  return (
    <div>
      <input type="text" value={login} onChange={(e) => setlogin(e.target.value)} placeholder="login" />
      <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" />
      <button onClick={handleLogin}>Login</button>
      <button onClick={handleRegister}>Register</button>
    </div>
  );
};

export default Auth;
