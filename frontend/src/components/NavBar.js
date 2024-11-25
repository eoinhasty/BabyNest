import React, {useEffect} from "react";
import {Link, useNavigate} from "react-router-dom";
import Cookies from "js-cookie";
import '../css/NavBar.css';

function NavBar() {
    const [loggedIn, setLoggedIn] = React.useState(!!Cookies.get('jwt'));
    const navigate = useNavigate();

    useEffect(() => {
        setLoggedIn(!!Cookies.get('jwt'));
    }, []);

    useEffect(() => {
        const interval = setInterval(() => {
            setLoggedIn(!!Cookies.get('jwt'));
        }, 500); // Check every 500ms

        return () => clearInterval(interval);
    }, []);

    const handleLogout = () => {
        Cookies.remove('jwt');
        setLoggedIn(false);
        navigate('/');
    };

    return (
        <nav className="navbar">
            <div className="navbar-logo">
                <Link to="/">
                    <img src="/BabyNest.jpg" alt="BabyNest Logo" className="navbar-logo-image"/>
                </Link>
            </div>
            <ul className="navbar-links">
                <li><Link to="/">Home</Link></li>
                <li><Link to="/products">Products</Link></li>
                {
                    loggedIn ? (
                        <>
                            <li><Link to="/cart">Cart {}</Link></li>
                            <li>
                                <button onClick={handleLogout} className={"logout-button"}>Logout</button>
                            </li>
                        </>
                    ) : (
                        <>
                            <li><Link to="/login">Login</Link></li>
                            <li><Link to="/register">Register</Link></li>
                        </>
                    )
                }
            </ul>
        </nav>
    );
}

export default NavBar;