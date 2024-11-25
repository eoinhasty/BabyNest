import React, {useEffect, useState} from 'react';
import Cookies from "js-cookie";
import { useNavigate } from 'react-router-dom';
import '../css/Login.css';

function Login() {
    const [credentials, setCredentials] = useState({username: '', password: ''});
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setCredentials({...credentials, [name]: value});
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        document.getElementById('login-button').disabled = true;
        setError('');

        try {
            const response = await fetch('http://localhost:8888/api/authenticate', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(credentials)
            });
            if(response.ok) {
                const data = await response.json();
                Cookies.set('jwt', data.jwt, {expires: 7}, {sameSite: 'strict'});
                navigate('/');
            } else if (response.status === 401) {
                setError('Login failed. Please check your email and password.');
            } else {
                setError('An error occurred. Please try again later.');
            }

            document.getElementById('login-button').disabled = false;
        } catch (error) {
            console.error('An error occurred during login:', error);
            setError('An error occurred. Please try again later.');
        }
    };

    useEffect(() => {
        if (Cookies.get('jwt')) {
            navigate('/'); // Redirect to homepage if already logged in
        }
    }, [navigate]);

    return (
        <div className={'login-container'}>
            <form className="login-form" onSubmit={handleSubmit}>
                <img src={"/BabyNest.jpg"} alt="Baby Nest Logo" className="login-logo"/>

                {error && <p className="login-error">{error}</p>}

                <div className={'login-input-wrapper'}>
                    <label htmlFor="username">Username:</label>
                    <input
                        type="text"
                        name="username"
                        id="username"
                        value={credentials.username}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <div className={'login-input-wrapper'}>
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        name="password"
                        id="password"
                        value={credentials.password}
                        onChange={handleInputChange}
                        required
                    />
                </div>
                <button type="submit" className={'login-button'} id={'login-button'}>Login</button>
            </form>
        </div>
    );
}

export default Login;