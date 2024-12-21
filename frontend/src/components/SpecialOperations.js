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
  const [resultPrint, setResultPrint] = useState(0);
  const navigate = useNavigate();

  return (
    <div>
      <div>
        <button onClick={() => sumNumberOfRooms().then((resp)=>{setResult(resp.data);setResultPrint(0);}).catch((e)=>{setResult('Ошибка');setResultPrint(0);})}>Сумма количеств комнат</button>
      </div>
      <div>
        <button onClick={() => countWithViewLower(view).then((resp)=>{setResult(resp.data);setResultPrint(0);}).catch((e)=>{setResult('Выберите один из возможных вариантов вида из окна');setResultPrint(0);})}>Количество с видом хуже чем</button>
        <select value={view} onChange={(e) => setView(e.target.value)}> 
          <option value="">Select</option>
          <option value="STREET">Улица</option>
          <option value="BAD">Плохой</option>
          <option value="TERRIBLE">К&Б</option>
        </select>
      </div>
      <div>
        <button onClick={() => getAllWithTimeToMetroByTransportLowerThan(timeToMetroByTransport).then((resp)=>{setResult(resp.data);setResultPrint(1);}).catch((e)=>{setResult('Введите число');setResultPrint(0);})}>Время до метро на транспорте меньше чем</button>
        <input type="number" value={timeToMetroByTransport} onChange={(e) => setTimeToMetroByTransport(e.target.value)} placeholder='минуты'/>
      </div>
      <div>
        <button onClick={() => getCheapestFlatWithBalcon().then((resp)=>{setResult([resp.data]);setResultPrint(1);}).catch((e)=>{setResult('Неизвестная ошибка');setResultPrint(0);})}>Самая дешевая квартира с балконом</button>
      </div>
      <div>
        <button onClick={() => getTheMostExpensiveFlat(flat1,flat2,flat3).then((resp)=>{setResult([resp.data]);setResultPrint(1);}).catch((e)=>{setResult("Одна из квартир не найдена");setResultPrint(0);})}>Самая дорогая из трех квартир</button>
        <input type="number" value={flat1} onChange={(e) => setFlat1(e.target.value)} placeholder='id первой квартиры'/>
        <input type="number" value={flat2} onChange={(e) => setFlat2(e.target.value)} placeholder='id второй квартиры'/>
        <input type="number" value={flat3} onChange={(e) => setFlat3(e.target.value)} placeholder='id третьей квартиры'/>
      </div>
      {(resultPrint == 0) && <p>{result}</p>}
      {(resultPrint == 1) && ((result.length > 0) ? (
        <table>
          <thead>
            <tr>
              <th>ID</th>
              <th>Название</th>
              <th>Площать</th>
              <th>Цена</th>
              <th>Наличие балкона</th>
              <th>Время до метро пешком</th>
              <th>Количество комнат</th>
              <th>Время до метро на транспорте</th>
              <th>Мебель</th>
              <th>Вид из окон</th>
              <th>Координата X</th>
              <th>Координата Y</th>
            </tr>
          </thead>
          <tbody>
            {result.map(flat => (
              <tr key={flat.id}>
                <td>{flat.id}</td>
                <td>{flat.name}</td>
                <td>{flat.area}</td>
                <td>{flat.price}</td>
                <td>{flat.balcony ? 'Есть' : 'Нет'}</td>
                <td>{flat.timeToMetroOnFoot}</td>
                <td>{flat.numberOfRooms}</td>
                <td>{flat.timeToMetroByTransport}</td>
                <td>{flat.furnish}</td>
                <td>{flat.view}</td>
                <td>{flat.coordinates.x}</td>
                <td>{flat.coordinates.y}</td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <p>Нет результатов. Может пора это исправлять?</p>
      ))}
    </div>
  );
};

export default SpecialOperations;
