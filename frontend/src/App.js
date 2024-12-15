import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import FlatList from './components/FlatList';
import FlatForm from './components/FlatForm';
import Auth from './components/Auth';
import AdminDashboard from './components/AdminDashboard';
import PrivateRoute from './components/PrivateRoute';
import { AuthProvider } from './context/AuthContext';

const App = () => {
  return (
    <AuthProvider>
      <Router>
        <Routes>
          <Route path="/auth" element={<Auth />} />
          <Route path="/flats" element={<FlatList />} />
          <Route path="/flats/new" element={<FlatForm />} />
          <Route path="/flats/:id/edit" element={<FlatForm />} />
          <Route path="/admin" element={<PrivateRoute><AdminDashboard /></PrivateRoute>} />
        </Routes>
      </Router>
    </AuthProvider>
  );
};

export default App;
