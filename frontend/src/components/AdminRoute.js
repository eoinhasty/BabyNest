import React from "react";
import { Navigate } from "react-router-dom";
import Cookies from "js-cookie";
import {jwtDecode} from "jwt-decode";

const ProtectedRoute = ({ children }) => {
    const jwt = Cookies.get("jwt");
    const decodedJwt = jwtDecode(jwt);

    const isAdmin = decodedJwt?.authorities?.includes("ROLE_ADMIN");

    if (!isAdmin) {
        return <Navigate to="/login" replace />;
    }

    try {
        const decoded = jwtDecode(jwt);
        const currentTime = Date.now() / 1000;
        if (decoded.exp < currentTime) {
            return <Navigate to="/login" replace />;
        }
    } catch (error) {
        console.error("Invalid token:", error);
        return <Navigate to="/login" replace />;
    }

    // If token is valid, render the children components
    return children;
};

export default ProtectedRoute;
