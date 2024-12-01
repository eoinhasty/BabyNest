import './css/App.css';
import React from "react";
import AllProducts from "./pages/AllProducts";
import ProductDrilldown from "./pages/ProductDrilldown";
import NavBar from "./components/NavBar";
import Register from "./pages/Register";
import Login from "./pages/Login";
import Cart from "./pages/Cart";
import Checkout from "./pages/Checkout";
import ProtectedRoute from "./components/ProtectedRoute";
import AdminRoute from "./components/AdminRoute";
import MyOrders from "./pages/MyOrders";
import OrderDrilldown from "./pages/OrderDrilldown";
import AdminDashboard from "./pages/AdminDashboard";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";

function App() {
    return (
        <Router>
            <div>
                <NavBar />
                <Routes>
                    <Route path="/" element={<AllProducts/>}/>
                    <Route path="/products" element={<AllProducts/>}/>
                    <Route path="/products/:productId" element={<ProductDrilldown/>}/>
                    <Route path={"/cart"} element={
                        <ProtectedRoute>
                           <Cart/>
                        </ProtectedRoute>
                    }/>
                    <Route path={"/checkout"} element={
                        <ProtectedRoute>
                            <Checkout/>
                        </ProtectedRoute>
                    }/>
                    <Route path={"/orders"} element={
                        <ProtectedRoute>
                            <MyOrders/>
                        </ProtectedRoute>
                    }/>
                    <Route path={"/orders/:orderId"} element={
                        <ProtectedRoute>
                            <OrderDrilldown/>
                        </ProtectedRoute>
                    }/>
                    <Route path={"/admin"} element={
                        <AdminRoute>
                            <AdminDashboard/>
                        </AdminRoute>
                    }/>
                    <Route path={"/login"} element={<Login/>}/>
                    <Route path={"/register"} element={<Register/>}/>
                    <Route path={"*"} element={<h1>Not Found</h1>}/>
                </Routes>
            </div>
        </Router>
    );
}

export default App;
