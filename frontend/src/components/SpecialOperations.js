import React, { useState } from 'react';
import { sumNumberOfRooms, countWithViewLower, getAllWithTimeToMetroByTransportLowerThan, getCheapestFlatWithBalcon, getTheMostExpensiveFlat } from '../services/api';
import { useNavigate } from 'react-router-dom';

const SpecialOperations = () => {
  const [result, setResult] = useState('');
  const [view, setView] = useState('');
  const [timeToMetroByTransport, setTimeToMetroByTransport] = useState(0);
  const [flat1, setFlat1] = useState(1);
  const [flat2, setFlat2] = useState(1);
  const [flat3, setFlat3] = useState(1);
  const navigate = useNavigate();

  return (
    <div>
      <div>
        <button onClick={()=>navigate("/flats")}>Вернуться к квартиркам</button>
      </div>
      <div>
        <button onClick={() => sumNumberOfRooms().then((resp)=>setResult(resp.data)).catch((e)=>setResult('Ошибка'))}>Сумма количеств комнат</button>
      </div>
      <div>
        <button onClick={() => countWithViewLower(view).then((resp)=>setResult(resp.data)).catch((e)=>setResult('Выберите один из возможных вариантов вида из окна'))}>Количество с видом хуже чем</button>
        <select value={view} onChange={(e) => setView(e.target.value)}> 
          <option value="">Select</option>
          <option value="STREET">Улица</option>
          <option value="BAD">Плохой</option>
          <option value="TERRIBLE">К&Б</option>
        </select>
      </div>
      <div>
        <button onClick={() => getAllWithTimeToMetroByTransportLowerThan(timeToMetroByTransport).then((resp)=>setResult(resp.data)).catch((e)=>setResult('Введите число'))}>Время до метро на транспорте меньше чем</button>
        <input type="number" value={timeToMetroByTransport} onChange={(e) => setTimeToMetroByTransport(e.target.value)} placeholder='минуты'/>
      </div>
      <div>
        <button onClick={() => getCheapestFlatWithBalcon().then((resp)=>setResult(resp.data)).catch((e)=>setResult('Неизвестная ошибка'))}>Самая дешевая квартира с балконом</button>
      </div>
      <div>
        <button onClick={() => getTheMostExpensiveFlat(flat1,flat2,flat3).then((resp)=>setResult(resp.data)).catch((e)=>setResult("Одна из квартир не найдена"))}>Самая дорогая из трех квартир</button>
        <input type="number" value={flat1} onChange={(e) => setFlat1(e.target.value)} placeholder='id первой квартиры'/>
        <input type="number" value={flat2} onChange={(e) => setFlat2(e.target.value)} placeholder='id второй квартиры'/>
        <input type="number" value={flat3} onChange={(e) => setFlat3(e.target.value)} placeholder='id третьей квартиры'/>
      </div>
      {result && <div>{result}</div>}
    </div>
  );
};

export default SpecialOperations;
