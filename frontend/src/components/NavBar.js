import React, { useEffect, useState } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import Cookies from "js-cookie";
import {jwtDecode} from "jwt-decode";
import '../css/NavBar.css';

function NavBar() {
    const [decodedJwt, setDecodedJwt] = useState(null);
    const navigate = useNavigate();
    const location = useLocation();

    // Utility to check if the user is logged in
    const checkLoggedIn = () => {
        const jwt = Cookies.get('jwt');
        if (jwt) {
            try {
                const decoded = jwtDecode(jwt);
                setDecodedJwt(decoded);
            } catch (e) {
                console.error("Error decoding JWT:", e);
                setDecodedJwt(null);
            }
        } else {
            setDecodedJwt(null);
        }
    };

    useEffect(() => {
        checkLoggedIn();
    }, [location]);

    const isLoggedIn = !!decodedJwt;
    const isCustomer = decodedJwt?.authorities?.includes("ROLE_CUSTOMER");
    const isAdmin = decodedJwt?.authorities?.includes("ROLE_ADMIN");
    
    const handleLogout = () => {
        Cookies.remove('jwt');
        setDecodedJwt(null);
        navigate('/');
    };

    return (
        <nav className="navbar">
            <div className="navbar-logo">
                <Link to="/">
                    <img src="/BabyNest.jpg" alt="BabyNest Logo" className="navbar-logo-image" />
                </Link>
            </div>
            <ul className="navbar-links">
                <li><Link to="/">Home</Link></li>
                <li><Link to="/products">Products</Link></li>
                {isLoggedIn ? (
                    <>
                        {isCustomer && (
                            <>
                                <li><Link to="/cart">Cart</Link></li>
                                <li><Link to="/orders">My Orders</Link></li>
                            </>
                        )}
                        {isAdmin && (
                            <>
                                <li><Link to="/admin">Admin Dashboard</Link></li>
                            </>
                        )}
                        <li>
                            <button onClick={handleLogout} className="logout-button">Logout</button>
                        </li>
                        <li className="navbar-user">Hi {decodedJwt?.sub}!</li>
                    </>
                ) : (
                    <>
                        <li><Link to="/login">Login</Link></li>
                        <li><Link to="/register">Register</Link></li>
                    </>
                )}
            </ul>
        </nav>
    );
}

export default NavBar;
