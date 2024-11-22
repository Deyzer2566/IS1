import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import FlatList from './components/FlatList';
import FlatForm from './components/FlatForm';
import Auth from './components/Auth';
import SpecialOperations from './components/SpecialOperations';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/flats/:id/edit" element={<FlatForm />} />
        <Route path="/flats/new" element={<FlatForm />} />
        <Route path="/flats" element={<FlatList />} />
        <Route path="/auth" element={<Auth />} />
        <Route path="/special" element={<SpecialOperations />} />
        <Route path="/" element={<FlatList />} />
      </Routes>
    </Router>
  );
};

export default App;
