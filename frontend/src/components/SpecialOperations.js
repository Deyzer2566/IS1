import React, { useState } from 'react';
import { specialOperation } from '../services/api';

const SpecialOperations = () => {
  const [result, setResult] = useState(null);

  const handleOperation = (operation) => {
    specialOperation(operation)
      .then(response => setResult(response.data))
      .catch(error => console.error(error));
  };

  return (
    <div>
      <button onClick={() => handleOperation('sumNumberOfRooms')}>Sum Number of Rooms</button>
      <button onClick={() => handleOperation('countViewLessThan')}>Count View Less Than</button>
      <button onClick={() => handleOperation('findCheapestWithBalcony')}>Find Cheapest with Balcony</button>
      <button onClick={() => handleOperation('findMostExpensiveFromThree')}>Find Most Expensive from Three</button>
      {result && <div>{JSON.stringify(result)}</div>}
    </div>
  );
};

export default SpecialOperations;
