import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login as loginApi, register} from '../services/api';
import { useAuth } from '../context/AuthContext';

const Auth = () => {
  const [loginName, setLoginName] = useState('');
  const [password, setPassword] = useState('');
  const [message, setMessage] = useState('');
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleLogin = () => {
    loginApi({ login: loginName, password })
      .then(response => {
        const { token, isAdmin } = response.data;
        login({ token, isAdmin });
        navigate('/flats');
      })
      .catch(error => {
        setMessage(error.response.data.message);
      });
  };

  const handleRegister = () => {
    register({ login: loginName, password })
      .then(response => {
        const { token, isAdmin } = response.data;
        login({ token, isAdmin });
        navigate('/flats');
      })
      .catch(error => {
        setMessage(error.response.data.message);
      });
  };

  return (
    <div>
      <input type="text" value={loginName} onChange={(e) => setLoginName(e.target.value)} placeholder="Login" />
      <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} placeholder="Password" />
      <button onClick={handleLogin}>Login</button>
      <button onClick={handleRegister}>Register</button>
      {message && <p>{message}</p>}
    </div>
  );
};

export default Auth;
