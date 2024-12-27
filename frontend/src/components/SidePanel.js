import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { getAdminRights } from '../services/api';

const SidePanel = ( { children }) => {
    const handleRequestAdminRights = () => {
        getAdminRights()
        .then(response => {
            
        })
        .catch(error => {
            
        });
        };

    const {user, logout} = useAuth();
    const navigate = useNavigate();

    return (
        <div>
            {user && (<button onClick={()=>{logout(); navigate("/auth");}}>Выйти из аккаунта</button>)}
            {!user && (<button onClick={()=>{navigate("/auth");}}>Авторизоваться</button>)}
            {user && user.isAdmin && (<button onClick={()=>navigate("/admin")}>Админ панель</button>)}
            {user && !user.isAdmin && (<button onClick={handleRequestAdminRights}>Мне повезет!</button>)}
            {<button onClick={()=>navigate("/flats")}>Квартиры на Циан 2.0</button>}
            {<button onClick={()=>navigate("/visualisation")}>Квартиры Циан 2.0 на карте</button>}
            {user && user.login && (<p>Вы авторизированы как {user.login}</p>)}
            { children }
        </div>
    )
}
export default SidePanel;