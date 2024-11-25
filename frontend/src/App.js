import './css/App.css';
import React from "react";
import AllProducts from "./pages/AllProducts";
import ProductDrilldown from "./pages/ProductDrilldown";
import NavBar from "./components/NavBar";
import Register from "./pages/Register";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Login from "./pages/Login";

function App() {
    return (
        <Router>
            <div>
                <NavBar />
                <Routes>
                    <Route path="/" element={<AllProducts/>}/>
                    <Route path="/products" element={<AllProducts/>}/>
                    <Route path="/products/:productId" element={<ProductDrilldown/>}/>
                    <Route path={"/cart"} element={<h1>Cart</h1>}/>
                    <Route path={"/login"} element={<Login/>}/>
                    <Route path={"/register"} element={<Register/>}/>
                    <Route path={"*"} element={<h1>Not Found</h1>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;
