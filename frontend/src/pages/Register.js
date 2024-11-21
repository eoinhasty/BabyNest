import React from 'react';
import '../css/Register.css';

function Register() {
    return (
        <div className="register-container">

            <form className="register-form">
                <img src={"/BabyNest.jpg"} alt="Baby Nest Logo" className="register-logo"/>

                <div className="register-input-wrapper">
                    <label htmlFor="username">Username:</label>
                    <input
                        type="text"
                        id="username"
                        name="username"
                    />
                </div>

                <div className="register-input-wrapper">
                    <label htmlFor="password">Password:</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                    />
                </div>

                <div className="register-input-wrapper">
                    <label htmlFor="email">Email:</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                    />
                </div>

                <button type="submit" className="register-button">Register</button>
            </form>
        </div>
    );
}

export default Register;