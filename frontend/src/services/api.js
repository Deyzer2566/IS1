import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(config => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const getFlats = (page) => api.get(`/flat`);
export const getFlat = (id) => api.get(`/flat/${id}`);
export const createFlat = (flat) => api.post('/flat', flat);
export const updateFlat = (id, flat) => api.put(`/flat/${id}`, flat);
export const deleteFlat = (id) => api.delete(`/flat/${id}`);
export const login = (credentials) => api.post('/management/auth', credentials);
export const register = (credentials) => api.post('/management/reg', credentials);
export const specialOperation = (operation) => api.get(`/flat/special/${operation}`);
export const getHouses = () => api.get('/houses');

export default api;
