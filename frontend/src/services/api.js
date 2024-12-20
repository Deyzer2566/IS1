import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  headers: {
    'Content-Type': 'application/json',
  },
});

api.interceptors.request.use(config => {
  if(localStorage.getItem('user') == null)
    return config;
  const token = JSON.parse(localStorage.getItem('user'))['token'];
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const getFlats = (page, filter = '', filterValue = '', sortField = '', sortOrder = 'asc') => {
  let url = `/flat?`;
  if (filter) {
    url += `&filterBy=${filter}&filterValue=${filterValue}`;
  }
  if (sortField) {
    url += `&sortBy=${sortField}&desc=${sortOrder}`;
  }
  return api.get(url);
};
export const getFlat = (id) => api.get(`/flat/${id}`);
export const createFlat = (flat) => api.post('/flat', flat);
export const updateFlat = (id, flat) => api.put(`/flat/${id}`, flat);
export const deleteFlat = (id) => api.delete(`/flat/${id}`);
export const login = (credentials) => api.post('/management/auth', credentials);
export const register = (credentials) => api.post('/management/reg', credentials);
export const getHouses = () => api.get('/flat/houses');
export const getApplications = () => api.get('/management/applications');

export const approveApplication = (applicationId) => api.post(`/management/makeAdmin/${applicationId}`);
export const rejectApplication = (applicationId) => api.post(`/management/reject/${applicationId}`);
export const getAdminRights = () => api.post('/management/sigma');

export const sumNumberOfRooms = () => api.get("/smo/sumNumberOfRooms");
export const countWithViewLower = (view) => api.get(`/smo/countWithViewLower?view=${view}`);
export const getAllWithTimeToMetroByTransportLowerThan = (timeToMetroByTransport) => api.get(`/smo/getAllWithTimeToMetroByTransportLowerThan?timeToMetroByTransport=${timeToMetroByTransport}`);
export const getCheapestFlatWithBalcon = () => api.get("/smo/getCheapestHataWithBalcon");
export const getTheMostExpensiveFlat = (id1, id2, id3) => api.get(`/smo/theMostExpensiveFlat?flat1=${id1}&flat2=${id2}&flat3=${id3}`);
export default api;
