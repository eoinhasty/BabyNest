import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import Carousel from 'react-multi-carousel';
import 'react-multi-carousel/lib/styles.css';
import '../css/ProductDrilldown.css';
import Cookies from "js-cookie";

function ProductDrilldown() {

    const {productId} = useParams();
    const [product, setProduct] = useState(null);
    const [imageList, setImageList] = useState([]);
    const [error, setError] = useState('');
    const [message, setMessage] = useState('');

    const responsive = {
        desktop: {
            breakpoint: {max: 3000, min: 1024},
            items: 3,
            slidesToSlide: 1
        },
        tablet: {
            breakpoint: {max: 1024, min: 464},
            items: 2,
            slidesToSlide: 1
        },
        mobile: {
            breakpoint: {max: 464, min: 0},
            items: 1,
            slidesToSlide: 1
        }
    };

    const fetchApi = async () => {
        try {
            const response = await fetch(`http://localhost:8888/api/products/${productId}`);

            if(response.ok) {
                const data = await response.json();
                setProduct(data);
            } else {
                setProduct(null);
            }
        } catch (error) {
            setProduct(null);
        }
    };

    const fetchImages = async () => {
        try {
            const response = await fetch(`http://localhost:8888/api/products/${productId}/images`);

            if(response.ok) {
                const data = await response.json();
                setImageList(data);
            } else {
                setImageList([]);
            }
        } catch (error) {
            setImageList([]);
        }
    };

    useEffect(() => {
        fetchApi();
        fetchImages();
    }, [productId]);

    const formatPrice = (price) => {
        return new Intl.NumberFormat('en-IE', {style: 'currency', currency: 'EUR'}).format(price);
    };

    const formatDate = (date) => {
        return new Intl.DateTimeFormat('en-IE').format(date);
    };

    if (!product) {
        return <h2>Product not found</h2>
    }

    const approvedReviews = product.reviewList ? product.reviewList.filter(review => review.approved) : [];

    const addToCart = async () => {
        if(!Cookies.get('jwt')) {
            setError('Please login to add items to cart');
        } else {
            try {
                const response = await fetch ('http://localhost:8888/api/cart/add', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${Cookies.get('jwt')}`
                    },
                    body: JSON.stringify({productId: product.productId, quantity: 1})
                });



                if(response.ok) {
                    setMessage('Item added to cart');
                    setError('');
                } else if (response.status === 406) {
                    setError('Stock Quantity exceeded');
                    setMessage('');
                } else {
                    setError('Failed to add item to cart');
                    setMessage('');
                }
            } catch (error) {
                setError('Failed to add item to cart');
            }
        }
    }

    return (
        <div>
            <div className="product">
                <div className={"carousel-container"}>
                    {imageList.length > 2 ? (
                        <Carousel
                            responsive={responsive}
                            itemClass="carousel-item-padding-40-px"
                            infinite={true}
                            showDots={true}
                        >
                            {imageList.map((image, index) => (
                                <div key={index} style={{display: "flex", justifyContent: "center"}}>
                                    <img
                                        src={`http://localhost:8888/assets/images/large/${product.productId}/${image}`}
                                        alt={product.name}
                                        style={{width: "300px", height: "300px"}}
                                    />
                                </div>
                            ))}
                        </Carousel>
                    ) : imageList.length === 2 ? (
                        <div style={{display: "flex", justifyContent: "center", marginBottom: "30px", gap: "20px"}}>
                            {imageList.map((image, index) => (
                                <img
                                    key={index}
                                    src={`http://localhost:8888/assets/images/large/${product.productId}/${image}`}
                                    alt={product.name}
                                    style={{width: "300px", height: "300px", borderRadius: "10px"}}
                                />
                            ))}
                        </div>
                    ) : imageList.length === 1 ? (
                        <div style={{display: "flex", justifyContent: "center", marginBottom: "30px"}}>
                            <img
                                src={`http://localhost:8888/assets/images/large/${product.productId}/${imageList[0]}`}
                                alt={product.name}
                                style={{width: "300px", height: "300px", borderRadius: "10px"}}
                            />
                        </div>
                    ) : (
                        <p>No images available</p>
                    )}
                </div>

                <h2>{product.name}</h2>
                <p>{product.description}</p>
                <p>{formatPrice(product.price)}</p>
                {product.stockQuantity === 0 ? (
                    <p className="outOfStock">Item is currently unavailable</p>
                ) : product.stockQuantity < 10 ? (
                    <p className="lowStock">
                        Only {product.stockQuantity} left in stock - Low stock level!
                    </p>
                ) : (
                    <p className="inStock">{product.stockQuantity} available</p>
                )}

                {error && <p className="addToCart-error">{error}</p>}
                {message && <p className="addToCart-success">{message}</p>}

                <button onClick={addToCart} disabled={product.stockQuantity === 0}>Add to Cart</button>

                <h2>Category: {product.category?.name}</h2>
                <p>{product.category?.description}</p>

                <h2>Reviews:</h2>
                {approvedReviews.length > 0 ? (
                    <ul>
                        {approvedReviews.map((review, index) => (
                            <li key={index}>
                                <p>Rating: {review.rating}/5 ★</p>
                                <p>Review Date: {formatDate(review.createdAt.Date)}</p>
                                <p>{review.reviewText}</p>
                            </li>
                        ))}
                    </ul>
                ) : (
                    <p>No reviews found</p>
                )}
            </div>
        </div>
    )
        ;
}

export default ProductDrilldown;