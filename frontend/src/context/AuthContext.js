import React, { createContext, useContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
//   const [user, setUser] = useState(null);
//   console.log('authprovidercreds',user);
//   useEffect(() => {
//     const storedUser = JSON.parse(localStorage.getItem('user'));
//     console.log('authprovideruseeffect', localStorage.getItem('user'))
//     if (storedUser) {
//       setUser(storedUser);
//     }
//   }, []);

  const [user, setUser] = useState(()=>{
    const storedUser = JSON.parse(localStorage.getItem('user'));
    return storedUser;
  });

  const navigate = useNavigate();

  const login = (userData) => {
    setUser(userData);
    localStorage.setItem('user', JSON.stringify(userData));
  };

  const logout = () => {
    setUser(null);
    localStorage.removeItem('user');
    navigate("/auth");
  };

  return (
    <AuthContext.Provider value={{ user, login, logout }}>
      {window.location.pathname != "/auth" && <button onClick={logout}>Выйти из аккаунта</button>}
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
