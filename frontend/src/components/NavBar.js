import React, {useEffect} from "react";
import {Link, useNavigate} from "react-router-dom";
import Cookies from "js-cookie";
import {jwtDecode} from "jwt-decode";
import '../css/NavBar.css';

function NavBar() {
    const [loggedIn, setLoggedIn] = React.useState(!!Cookies.get('jwt'));
    const [decodedJwt, setDecodedJwt] = React.useState(null);
    const navigate = useNavigate();

    const isCustomer = decodedJwt?.authorities?.includes("ROLE_CUSTOMER");
    const isAdmin = decodedJwt?.authorities?.includes("ROLE_ADMIN");

    useEffect(() => {
        const jwt = Cookies.get('jwt');
        setLoggedIn(!!jwt);
        if(jwt) {
            try{
                const decoded = jwtDecode(jwt);
                setDecodedJwt(decoded);
            } catch (e) {
                console.log("Error decoding JWT: " + e);
            }
        }
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
                            {isCustomer && (
                                <li><Link to="/cart">Cart</Link></li>
                            )}
                            {isAdmin && (
                                <></>
                            )}
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