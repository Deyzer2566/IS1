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
            {(window.location.pathname != "/auth") && (<button onClick={()=>{logout(); navigate("/auth");}}>Выйти из аккаунта</button>)}
            {user && user.isAdmin && (<button onClick={()=>navigate("/admin")}>Админ панель</button>)}
            {user && !user.isAdmin && (<button onClick={handleRequestAdminRights}>Мне повезет</button>)}
            {<button onClick={()=>navigate("/flats")}>Квартиры на Циан 2.0</button>}
            { children }
        </div>
    )
}
export default SidePanel;