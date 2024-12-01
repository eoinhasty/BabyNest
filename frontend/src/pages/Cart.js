import Cookies from "js-cookie";
import React, {useEffect, useState} from "react";
import '../css/Cart.css';
import '../css/QuantityInput.css';
import {useNavigate} from "react-router-dom";

function Cart() {
    const [cart, setCart] = useState({});
    const [errorMessage, setErrorMessage] = useState('');
    const navigate = useNavigate();

    const fetchApi = async () => {
        const response = await fetch('http://localhost:8888/api/cart/', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${Cookies.get('jwt')}`
            }
        });

        if (response.ok) {
            const data = await response.json();
            setCart(data.cartItemList);
        } else {
            console.log('Failed to fetch cart');
        }
    };

    useEffect(() => {
        fetchApi();
    }, []);

    const updateCart = async (cartItemId, quantity) => {
        try {
            const response = await fetch(`http://localhost:8888/api/cart/update/${cartItemId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Cookies.get('jwt')}`
                },
                body: JSON.stringify({quantity})
            });

            if (!response.ok) {
                console.log('Failed to update cart');
                setErrorMessage('Failed to update cart');
            } else {
                const updatedCart = await response.json();
                setCart(updatedCart.cartItemList);
            }
        } catch (error) {
            console.log('Failed to update cart', error);
            setErrorMessage('Failed to update cart');
        }
    }

    const handleRemove = async (cartItemId) => {
        try {
            const response = await fetch(`http://localhost:8888/api/cart/remove`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Cookies.get('jwt')}`
                },
                body: JSON.stringify({cartItemId})
            });

            if (!response.ok) {
                console.log('Failed to remove item from cart');
                setErrorMessage('Failed to remove item from cart');
            } else {
                const updatedCart = await response.json();
                setCart(updatedCart.cartItemList);
            }
        } catch (error) {
            console.log('Failed to remove item from cart', error);
            setErrorMessage('Failed to remove item from cart');
        }
    }

    const handleClearCart = async () => {
        try {
            const response = await fetch(`http://localhost:8888/api/cart/clear`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Cookies.get('jwt')}`
                }
            });

            if (!response.ok) {
                console.log('Failed to clear cart');
                setErrorMessage('Failed to clear cart');
            } else {
                const updatedCart = await response.json();
                setCart(updatedCart.cartItemList);
            }
        } catch (error) {
            console.log('Failed to clear cart', error);
            setErrorMessage('Failed to clear cart');
        }
    }

    const handleCheckout = () => {
        if (cart.length === 0) {
            setErrorMessage('Your cart is empty.');
            return;
        }

        navigate('/checkout');
    };

    const handleDecrease = (cartItemId, currentQuantity) => {
        const newValue = Math.max(currentQuantity - 1, 1);
        setCart((prevCart) =>
            prevCart.map((item) =>
                item.cartItemId === cartItemId ? {...item, quantity: newValue} : item
            )
        );
        updateCart(cartItemId, newValue);
    }

    const handleIncrease = (cartItemId, currentQuantity, stockQuantity) => {
        const newValue = Math.min(currentQuantity + 1, stockQuantity);
        setCart((prevCart) =>
            prevCart.map((item) =>
                item.cartItemId === cartItemId ? {...item, quantity: newValue} : item
            )
        );
        updateCart(cartItemId, newValue);
    };

    const handleInputChange = (cartItemId, value) => {
        const newValue = parseInt(value);
        if (!isNaN(newValue) && newValue >= 1) {
            setCart((prevCart) =>
                prevCart.map((item) =>
                    item.cartItemId === cartItemId ? {...item, quantity: newValue} : item
                )
            );
        }
    };

    const handleBlur = (cartItemId, value) => {
        const newValue = parseInt(value);
        if (!isNaN(newValue) && newValue >= 1) {
            setCart((prevCart) =>
                prevCart.map((item) =>
                    item.cartItemId === cartItemId ? {...item, quantity: newValue} : item
                )
            );
            updateCart(cartItemId, newValue);
        }
    }

    //Format price to EUR
    const formatPrice = (price) => {
        return new Intl.NumberFormat('en-IE', {style: 'currency', currency: 'EUR'}).format(price);
    }

    return (
        <div className={"cart-page"}>
            {errorMessage && <p style={{color: "red"}}>{errorMessage}</p>}
            <table>
                <thead>
                <tr>
                    <th></th>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                {
                    cart && cart.length > 0 ? (
                        cart.map((item, index) => (
                            <tr key={index}>
                                <td>
                                    <a href={`/products/${item.product?.productId}`}>
                                        <img
                                            src={'http://localhost:8888/assets/images/thumbs/' + item.product?.productId + '/' + item.product?.feature_image}
                                            alt={item.product?.name}
                                            width={50}
                                        />
                                    </a>
                                </td>
                                <td>{item.product?.name}</td>
                                <td>
                                    <div className="quantity">
                                        <button
                                            className="minus"
                                            aria-label="Decrease"
                                            onClick={() => handleDecrease(item.cartItemId, item.quantity)}
                                            disabled={item.quantity <= 1}
                                        >
                                            &#8722;
                                        </button>
                                        <input
                                            type="number"
                                            className="input-box"
                                            value={item.quantity}
                                            min="1"
                                            max={item.product?.stockQuantity}
                                            onChange={(e) => handleInputChange(item.cartItemId, e.target.value, item.product?.stockQuantity)}
                                            onBlur={(e) => handleBlur(item.cartItemId, e.target.value, item.product?.stockQuantity)}
                                        />
                                        <button
                                            className="plus"
                                            aria-label="Increase"
                                            onClick={() => handleIncrease(item.cartItemId, item.quantity, item.product?.stockQuantity)}
                                            disabled={item.quantity >= item.product?.stockQuantity}
                                        >
                                            &#43;
                                        </button>
                                    </div>
                                </td>
                                <td>{formatPrice(item.product?.price * item.quantity)}</td>
                                <td>
                                    <button onClick={() => handleRemove(item.cartItemId)}
                                            className={"remove-item"}>Remove
                                    </button>
                                </td>
                                <td>
                                    {item.quantity >= item.product.stockQuantity ? (
                                        <span className={"quantity-warning"}
                                              title={"Maximum quantity reached"}>&#x24D8;</span>
                                    ) : (
                                        <span className={"quantity-warning"}>&nbsp;</span>
                                    )}
                                </td>
                            </tr>
                        ))
                    ) : (
                        <tr>
                            <td colSpan={6}><p>Cart is empty. View our products <a href={'/products'}>here</a>!</p></td>
                        </tr>
                    )
                }
                </tbody>
            </table>

            {cart && cart.length > 0 && (
                <div>
                    <p className={"total-price"}>Total: {formatPrice(cart.reduce((acc, item) => acc + item.product.price * item.quantity, 0))}</p>
                    <button onClick={handleCheckout} className={"checkout"}>Continue to Checkout</button>
                    <button onClick={handleClearCart} className={"clear-cart"}>Clear Cart</button>
                </div>
            )}
        </div>
    );
}

export default Cart;