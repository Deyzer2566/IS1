import React from 'react';
import { BrowserRouter as Router, Route, Routes, Navigate } from 'react-router-dom';
import FlatList from './components/FlatList';
import FlatForm from './components/FlatForm';
import Auth from './components/Auth';
import AdminDashboard from './components/AdminDashboard';
import PrivateRoute from './components/PrivateRoute';
import { AuthProvider } from './context/AuthContext';
import SpecialOperations from './components/SpecialOperations';
import SidePanel from './components/SidePanel';
import ObjectVisualization from './components/ObjectVisualisation';
const App = () => {

  return (
    <AuthProvider>
      <Router>
        <SidePanel>
          <Routes>
            <Route path="/auth" element={<Auth />} />
            <Route path="/flats" element={<FlatList />} />
            <Route path="/flats/new" element={<FlatForm />} />
            <Route path="/flats/:id/edit" element={<FlatForm />} />
            <Route path="/admin" element={<PrivateRoute><AdminDashboard /></PrivateRoute>} />
            <Route path="/smo" element={<SpecialOperations />} />
            <Route path="/" element={<Navigate to = "/flats" />} />
            <Route path="/visualisation" element={<ObjectVisualization />} />
          </Routes>
        </SidePanel>
      </Router>
    </AuthProvider>
  );
};

export default App;
