import React, {useState} from 'react';
import '../css/Register.css';
import {useNavigate} from "react-router-dom";
import Cookies from "js-cookie";

function Register() {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
        phone: '',
    });

    const [error, setError] = useState('');
    const navigate = useNavigate();

    if(Cookies.get('jwt')) {
        navigate('/');
    }

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;

        if (!passwordRegex.test(formData.password)) {
            setError('Password must contain at least 8 characters, one uppercase letter, one lowercase letter, one number and one special character.');
            return;
        }

        try {
            const response = await fetch('http://localhost:8888/api/customers/registerCustomer', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(formData),
            });

            if (!response.ok) {
                setError('Registration failed');
            } else {
                setFormData({
                    firstName: '',
                    lastName: '',
                    email: '',
                    password: '',
                });
                setError('');
                navigate('/login');
            }
        } catch (error) {
            console.error('An error occurred during Registration:', error);
            setError('An error occurred. Please try again later.');
        }
    };

    return (
        <div className="register-container">

            <form className="register-form" onSubmit={handleSubmit}>
                <img src={"/BabyNest.jpg"} alt="Baby Nest Logo" className="register-logo"/>

                {error && <p className="register-error">{error}</p>}

                <div className="register-input-wrapper">
                    <label htmlFor="firstName">First Name:</label>
                    <input
                        type="text"
                        id="firstName"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleInputChange}
                        required
                    />
                </div>

                <div className="register-input-wrapper">
                    <label htmlFor="lastName">Last Name:</label>
                    <input
                        type="text"
                        id="lastName"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleInputChange}
                        required
                    />
                </div>

                <div className="register-input-wrapper">
                    <label htmlFor="phone">Mobile Phone:</label>
                    <input
                        type="tel"
                        id="phone"
                        name="phone"
                        value={formData.phone}
                        onChange={handleInputChange}
                        pattern={"^(08[356789])\\d{7}$"}
                        required
                    />
                </div>

                <div className="register-input-wrapper" >
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleInputChange}
                        required
                    />
                </div>

                <div className="register-input-wrapper">
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleInputChange}
                        required
                    />
                </div>

                <button type="submit" className="register-button">Register</button>
            </form>
        </div>
    );
}

export default Register;