import React from 'react';
import '../css/Register.css';

function Register() {
    const [formData, setFormData] = React.useState({
        firstName: '',
        lastName: '',
        email: '',
        password: '',
    });

    const [error, setError] = React.useState('');

    const handleInputChange = (e) => {
        const {name, value} = e.target;
        setFormData({
            ...formData,
            [name]: value
        });
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch('http://localhost:8888/api/customers/registerCustomer', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'  // Removed the charset=UTF-8 part
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