import {useEffect, useState} from "react";
import {Link} from "react-router-dom";
import DataTable from "datatables.net-react";
import DT from "datatables.net-dt";
import $ from "jquery";

DataTable.use(DT);

function AllProducts() {
    const [products, setProducts] = useState([])

    const fetchApi = async () => {
        const response = await fetch('http://localhost:8888/api/products/');

        const data = await response.json();
        setProducts(data);
    }

    useEffect(() => {
        fetchApi();
    }, []);

    useEffect(() => {
        if (products.length > 0) {
            $("#products").DataTable();
        }
    }, [products]);

    const formatPrice = (price) => {
        return new Intl.NumberFormat('en-IE', { style: 'currency', currency: 'EUR' }).format(price);
    }

    // return (
    //     <div className="App">
    //         <div className="products">
    //             {products.length > 0 ? (
    //             products.map((product) => (
    //                 <div key={product.productId} className="product">
    //                     <Link to={`/products/${product.productId}`}>
    //                         <img
    //                         src={`http://localhost:8888/assets/images/thumbs/${product.productId}/${product.feature_image}`}
    //                         alt={product.name}
    //                         ></img>
    //                     </Link>
    //                     <h2>{product.name}</h2>
    //                     <p>{product.description}</p>
    //                     <p>€{product.price}</p>
    //                     <p>{product.stockQuantity > 0 ? `${product.stockQuantity} available` : 'Out of Stock'}</p>
    //                 </div>
    //             ))
    //             ) : (
    //                 <h2>No products found</h2>
    //             )}
    //         </div>
    //     </div>
    // );

    return (
        <div className={"App"}>
            <table id={"products"} className={"display"}>
                <thead>
                <tr>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Price</th>
                    <th>Image</th>
                </tr>
                </thead>
                <tbody>
                {products.length > 0 ? (
                    products.map((product) => (
                    <tr key={product.productId}>
                        <td>{product.name}</td>
                        <td>{product.description}</td>
                        <td>{formatPrice(product.price)}</td>
                        <td>
                            <Link to={`/products/${product.productId}`}>
                                <img
                                    src={`http://localhost:8888/assets/images/thumbs/${product.productId}/${product.feature_image}`}
                                    alt={product.name}
                                    style={{width: '100px'}}
                                ></img>
                            </Link>
                        </td>
                    </tr>
                ))) : (
                    <tr>
                        <td colSpan={4}>No products found</td>
                    </tr>
                )}
                </tbody>
            </table>
        </div>
    )
}

export default AllProducts;