import React, { useState, useEffect } from 'react';
import Cookies from 'js-cookie';
import '../css/Checkout.css';
import {useNavigate} from "react-router-dom";

function Checkout() {
    const [addresses, setAddresses] = useState([]);
    const [selectedAddressId, setSelectedAddressId] = useState(null);
    const [newAddress, setNewAddress] = useState({
        streetAddress: '',
        city: '',
        county: '',
        postalCode: '',
        country: '',
    });
    const [editing, setEditing] = useState(false);
    const [error, setError] = useState('');

    const navigate = useNavigate();

    useEffect(() => {
        const fetchAddresses = async () => {
            try {
                const response = await fetch('http://localhost:8888/api/address/', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${Cookies.get('jwt')}`
                    }
                });

                if (response.ok) {
                    const data = await response.json();
                    setAddresses(data);
                    if(data.length > 0) {
                        setSelectedAddressId(data[0].addressId);
                    }
                } else {
                    console.log('Failed to fetch addresses');
                }
            } catch (error) {
                console.log('Failed to fetch addresses', error);
            }
        }

        fetchAddresses();
    }, []);

    const handleSaveAddress = async (e) => {
        const url = newAddress.addressId ? `http://localhost:8888/api/address/update` : 'http://localhost:8888/api/address/create';

        try {
            const response = await fetch(url, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Cookies.get('jwt')}`
                },
                body: JSON.stringify(newAddress)
            });

            if (response.ok) {
                const savedAddress = await response.json();

                setAddresses((prevAddresses) => {
                    if (newAddress.addressId) {
                        return prevAddresses.map((addr) => {
                            if (addr.addressId === savedAddress.addressId) {
                                return savedAddress;
                            }
                            return addr;
                        });
                    } else {
                        return [...prevAddresses, savedAddress];
                    }
                });

                setSelectedAddressId(savedAddress.addressId);

                setEditing(false);
                setNewAddress({
                    streetAddress: '',
                    city: '',
                    county: '',
                    postalCode: '',
                    country: '',
                });
                setError('');
            } else {
                setError('Failed to save address');
            }
        } catch (error) {
            setError('Failed to save address');
        }
    }

    const handleDeleteAddress = async (addressId) => {
        try {
            const response = await fetch(`http://localhost:8888/api/address/delete/${addressId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Cookies.get('jwt')}`
                }
            });

            if (response.ok) {
                setAddresses((prevAddresses) => prevAddresses.filter((addr) => addr.addressId !== addressId));
                setSelectedAddressId(addresses[0].addressId ? addresses[0].addressId : null);
                setError('');
            } else {
                setError('Failed to delete address');
            }
        } catch (error) {
            setError('Failed to delete address');
        }
    }

    const handleCheckout = async () => {
        if(!selectedAddressId) {
            setError('Please select an address');
            return;
        }

        try {
            const response = await fetch('http://localhost:8888/api/orders/create', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${Cookies.get('jwt')}`
                },
                body: JSON.stringify({
                    addressId: selectedAddressId
                })
            });

            if (response.ok) {
                const data = await response.json();
                navigate(`/`);
                alert('Order created');
                console.log('Order created', data);
            } else {
                setError('Failed to create order');
            }
        } catch (error) {
            setError('Failed to create order');
        }
    }

    return (
        <div className="address">
            {error && <p className={"error-message"}>{error}</p>}

            <h1>Select an Address</h1>
            <div className="address-list">
                {addresses.length > 0 ? (
                    <select
                        onChange={(e) => setSelectedAddressId(parseInt(e.target.value))}
                        value={selectedAddressId}
                    >
                        {addresses.map((addr) => (
                            <option key={addr.addressId} value={addr.addressId}>
                                {addr.streetAddress}, {addr.city}, Co. {addr.county}, {addr.country} - {addr.postalCode}
                            </option>
                        ))}
                    </select>
                ) : (
                    <p>No addresses available</p>
                )}
            </div>

            <button onClick={() => {
                setEditing(true);
                setNewAddress({
                    streetAddress: '',
                    city: '',
                    county: '',
                    postalCode: '',
                    country: '',
                });
            }}
                className={"add-button"}
            >Add New Address
            </button>
            {selectedAddressId && (
                <>
                    <button
                        onClick={() => {
                            const addr = addresses.find((a) => a.addressId === selectedAddressId);
                            setNewAddress(addr);
                            setEditing(true);
                        }}
                        className={"edit-button"}
                    >
                        Edit Address
                    </button>
                    <button
                        onClick={() => handleDeleteAddress(selectedAddressId)}
                        className={"delete-button"}
                    >
                        Delete Address
                    </button>
                </>
            )}

            {editing && (
                <div>
                    <h3>{newAddress.addressId ? 'Edit Address' : 'Add New Address'}</h3>
                    <form
                        onSubmit={(e) => {
                            e.preventDefault();
                            handleSaveAddress();
                        }}
                    >
                        <input
                            type="text"
                            name="streetAddress"
                            placeholder="Street"
                            value={newAddress.streetAddress}
                            required
                            onChange={(e) => setNewAddress({...newAddress, streetAddress: e.target.value})}
                        />
                        <input
                            type="text"
                            name="city"
                            placeholder="City"
                            value={newAddress.city}
                            required
                            onChange={(e) => setNewAddress({...newAddress, city: e.target.value})}
                        />
                        <input
                            type="text"
                            name="county"
                            placeholder="County"
                            value={newAddress.county}
                            required
                            onChange={(e) => setNewAddress({...newAddress, county: e.target.value})}
                        />
                        <input
                            type="text"
                            name="country"
                            placeholder="Country"
                            value={newAddress.country}
                            required
                            onChange={(e) => setNewAddress({...newAddress, country: e.target.value})}
                        />
                        <input
                            type="text"
                            name="postalCode"
                            placeholder="Postal Code"
                            value={newAddress.postalCode}
                            required
                            onChange={(e) => setNewAddress({...newAddress, postalCode: e.target.value})}
                        />
                        <button type="submit">Save Address</button>
                    </form>
                </div>
            )}
            <div>
                <button onClick={handleCheckout} className={"checkout-button"}>Checkout</button>
            </div>
        </div>
    )
}

export default Checkout;