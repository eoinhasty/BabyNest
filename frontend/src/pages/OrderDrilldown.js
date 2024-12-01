import React, {useState, useEffect} from 'react';
import Cookies from 'js-cookie';
import {useParams} from "react-router-dom";

function OrderDrilldown() {
    const {orderId} = useParams();
    const [order, setOrder] = useState({});
    const [orderItems, setOrderItems] = useState([]);

    const fetchApi = async () => {
        let orderUrl = `http://localhost:8888/api/orders/getOrder/${orderId}`;

        try {
            const orderResponse = await fetch(orderUrl, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Cookies.get('jwt')}`
                }
            });

            if(orderResponse.ok) {
                const orderData = await orderResponse.json();
                setOrder(orderData);
                setOrderItems(orderData.orderItemsList);
            } else {
                console.log('Order not found');
            }
        } catch (error) {
            console.log('Error fetching order data');
        }
    }

    useEffect(() => {
        fetchApi();
    }, []);

    const formatPrice = (price) => {
        return new Intl.NumberFormat('en-IE', {style: 'currency', currency: 'EUR'}).format(price);
    }

    const formatDate = (date) => {
        return new Date(date).toLocaleDateString('en-IE', {
            year: 'numeric',
            month: 'long',
            day: 'numeric'
        });
    }

    return (
        <div>
            <h2>Order Details</h2>
            <h3>Order Date: {formatDate(order.orderDate)}</h3>
            <h3>Status: {order.status}</h3>
            <h3>Total Amount: {formatPrice(order.totalAmount)}</h3>
            <h3>Estimated Shipping Date: {formatDate(order.estimatedShippingDate)}</h3>
            <h3>Items:</h3>
            <table>
                <thead>
                <tr>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Price</th>
                </tr>
                </thead>
                <tbody>
                {orderItems.map((item, index) => (
                    <tr key={index}>
                        <td>{item.product?.name}</td>
                        <td>{item.quantity}</td>
                        <td>{formatPrice(item.totalPrice)}</td>
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}

export default OrderDrilldown;