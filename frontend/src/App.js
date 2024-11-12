import './App.css';
import React from "react";
import AllProducts from "./pages/AllProducts";
import ProductDrilldown from "./pages/ProductDrilldown";
import {BrowserRouter as Router, Route, Routes} from "react-router-dom";

function App() {
    // return (
    //   <div className="App">
    //     <header className="App-header">
    //       <img src={logo} className="App-logo" alt="logo" />
    //       <p>
    //         Edit <code>src/App.js</code> and save to reload.
    //       </p>
    //       <a
    //         className="App-link"
    //         href="https://reactjs.org"
    //         target="_blank"
    //         rel="noopener noreferrer"
    //       >
    //         Learn React
    //       </a>
    //     </header>
    //   </div>
    // );

    return (
        <Router>
            <Routes>
                <Route path="/" element={<AllProducts />} />
                <Route path="/products/:productId" element={<ProductDrilldown />} />
            </Routes>
        </Router>
    );
}

export default App;
