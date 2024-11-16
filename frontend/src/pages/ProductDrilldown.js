import {useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import Carousel from 'react-multi-carousel';
import 'react-multi-carousel/lib/styles.css';
import '../css/ProductDrilldown.css';

function ProductDrilldown() {

    const {productId} = useParams();
    const [product, setProduct] = useState([]);
    const [imageList, setImageList] = useState([]);

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
        const response = await fetch(`http://localhost:8888/api/products/${productId}`);
        const data = await response.json();
        setProduct(data);
    };

    const fetchImages = async () => {
        const response = await fetch(`http://localhost:8888/api/products/${productId}/images`);
        const data = await response.json();
        setImageList(data);
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

    return (
        <div>
            <div className="product">
                <Carousel
                    responsive={responsive}
                    itemClass="carousel-item-padding-40-px"
                    infinite={true}
                    showDots={true}
                >
                    {imageList && imageList.map((image, index) => (
                        <div style={{display: "flex", justifyContent: "center"}}>
                            <img
                                key={index}
                                src={`http://localhost:8888/assets/images/large/${product.productId}/${image}`}
                                alt={product.name}
                                style={{width: "300px", height: "300px"}}
                            ></img>
                        </div>
                    ))}
                </Carousel>

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

                <h2>Category: {product.category?.name}</h2>
                <p>{product.category?.description}</p>

                <h2>Reviews:</h2>
                {approvedReviews.length > 0 ? (
                    <ul>
                        {approvedReviews.map((review, index) => (
                            <li key={index}>
                                <p>Rating: {review.rating}/5</p>
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
    );
}

export default ProductDrilldown;