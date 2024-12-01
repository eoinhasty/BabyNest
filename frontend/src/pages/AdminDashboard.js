import React, {useState, useEffect} from "react";
import Cookies from "js-cookie";

function AdminDashboard() {
    const [view, setView] = useState("unapprovedReviews");
    const [data, setData] = useState([]);
    const [loading, setLoading] = useState(false);
    const [selectedProduct, setSelectedProduct] = useState(null);
    const [productForm, setProductForm] = useState({
        productId: "",
        name: "",
        price: "",
        description: "",
        stockQuantity: "",
    });
    const [images, setImages] = useState([]);
    const [newImages, setNewImages] = useState([]);

    const fetchData = async () => {
        setLoading(true);
        setData([]);
        try {
            const url =
                view === "suspendedUsers"
                    ? ("http://localhost:8888/api/admin/users/suspended")
                    : view === "products"
                        ? "http://localhost:8888/api/products/"
                    : view === "archivedProducts"
                        ? "http://localhost:8888/api/admin/products/archived"
                        : "http://localhost:8888/api/admin/reviews";
            const response = await fetch(url, {
                headers: {
                    Authorization: `Bearer ${Cookies.get("jwt")}`,
                },
            });
            if (response.ok) {
                const result = await response.json();
                setData(result);
            } else {
                console.error("Failed to fetch data");
            }
        } catch (error) {
            console.error("Error fetching data:", error);
        }
        setLoading(false);
    };

    const fetchImages = async (productId) => {
        try {
            const response = await fetch(`http://localhost:8888/api/products/${productId}/images`);
            if (response.ok) {
                const data = await response.json();
                setImages(data);
            } else {
                setImages([]);
            }
        } catch (error) {
            setImages([]);
        }
    }

    const handleSuspendUser = async (userId) => {
        await fetch(`http://localhost:8888/api/admin/users/suspend/${userId}`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${Cookies.get("jwt")}`,
            },
        });
        await fetchData();
    };

    const handleActivateUser = async (userId) => {
        await fetch(`http://localhost:8888/api/admin/users/activate/${userId}`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${Cookies.get("jwt")}`,
            },
        });
        await fetchData();
    };

    const handleApproveReview = async (reviewId) => {
        await fetch(`http://localhost:8888/api/admin/reviews/${reviewId}/approve`, {
            method: "POST",
            headers: {
                Authorization: `Bearer ${Cookies.get("jwt")}`,
            },
        });
        await fetchData();
    };

    const handleUpdateProduct = async () => {
        try {
            console.log(productForm);

            await fetch(`http://localhost:8888/api/admin/products/update/${productForm.productId}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${Cookies.get("jwt")}`,
                },
                body: JSON.stringify(productForm),
            });
            alert("Product updated successfully");
            await fetchData();
            setSelectedProduct(null);
        } catch (error) {
            console.error("Error updating product:", error);
            alert("Failed to update product");
        }
    }

    const handleImageChange = (e) => {
        const files = Array.from(e.target.files);
        setNewImages((prev) => [...prev, ...files]); // Add new images to state
    };

    const removeImage = (index, type) => {
        if (type === "existing") {
            setImages((prev) => prev.filter((_, i) => i !== index)); // Remove from existing images
        } else {
            setNewImages((prev) => prev.filter((_, i) => i !== index)); // Remove from new images
        }
    };

    const handleUploadImages = async () => {

        if(!productForm.productId) {
            return;
        }

        const formData = new FormData();

        // Append existing images
        images.forEach((image) => {
            formData.append("existingImages", image); // Send the ID of the existing images to backend
        });

        // Append new images
        newImages.forEach((image) => {
            formData.append("newImages", image);
        });

        try {
            await fetch(`http://localhost:8888/api/admin/products/images/${productForm.productId}`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${Cookies.get("jwt")}`,
                },
                body: formData,
                credentials: "include",
            });
            alert("Images updated successfully");
        } catch (error) {
            console.error("Error uploading images:", error);
            alert("Failed to upload images");
        }
    };

    const handleArchiveProduct = async (productId) => {
        try {
            await fetch(`http://localhost:8888/api/admin/products/archive/${productId}`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${Cookies.get("jwt")}`,
                },
            });
            await fetchData();
        } catch (error) {
            console.error("Error archiving product:", error);
            alert("Failed to archive product");
        }
    }

    const handleUnarchiveProduct = async (productId) => {
        try {
            await fetch(`http://localhost:8888/api/admin/products/unarchive/${productId}`, {
                method: "POST",
                headers: {
                    Authorization: `Bearer ${Cookies.get("jwt")}`,
                },
            });
            await fetchData();
        } catch (error) {
            console.error("Error unarchiving product:", error);
            alert("Failed to unarchive product");
        }
    }

    useEffect(() => {
        fetchData();
    }, [view]);

    const handleProductFormChange = (event) => {
        const {name, value} = event.target;
        setProductForm((prevState) => ({
            ...prevState,
            [name]: value,
        }));
    }

    return (
        <div>
            <h1>Admin Dashboard</h1>

            <div>
                <button onClick={() => setView("suspendedUsers")}>
                    Show Suspended Users
                </button>
                <button onClick={() => setView("unapprovedReviews")}>
                    Show Unapproved Reviews
                </button>
                <button onClick={() => setView("products")}>
                    Manage Products
                </button>
                <button onClick={() => setView("archivedProducts")}>
                    Show Archived Products
                </button>
            </div>

            {loading ? (
                <p>Loading...</p>
            ) : view === "suspendedUsers" ? (
                <div>
                    <h2>Suspended Users</h2>
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Email</th>
                            <th>Name</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {data.map((user) => (
                            <tr key={user.userId}>
                                <td>{user.userId}</td>
                                <td>{user.email}</td>
                                <td>{user.firstName} {user.lastName}</td>
                                <td>
                                    <button onClick={() => handleActivateUser(user.userId)}>
                                        Activate
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            ) : view === "unapprovedReviews" ? (
                <div>
                    <h2>Unapproved Reviews</h2>
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Product</th>
                            <th>Review Text</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {data.map((review) => (
                            <tr key={review.id}>
                                <td>{review.reviewId}</td>
                                <td>{review.product?.name}</td>
                                <td>{review.reviewText}</td>
                                <td>
                                    <button onClick={() => handleApproveReview(review.reviewId)}>
                                        Approve
                                    </button>
                                </td>
                                <td>
                                    <button onClick={() => handleSuspendUser(review.customer.userId)}>
                                        Suspend User
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            ) : view === "archivedProducts" ? (
                <div>
                    <h2>Archived Products</h2>
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {data.map((product) => (
                            <tr key={product.productId}>
                                <td>{product.productId}</td>
                                <td>{product.name}</td>
                                <td>{product.price}</td>
                                <td>{product.stockQuantity}</td>
                                <td>
                                    <button onClick={() => handleUnarchiveProduct(product.productId)}>
                                        Unarchive
                                    </button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            ) : (
                <div>
                    <h2>Manage Products</h2>
                    <table>
                        <thead>
                        <tr>
                            <th>ID</th>
                            <th>Name</th>
                            <th>Price</th>
                            <th>Stock</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {data.map((product) => (
                            <tr key={product.productId}>
                                <td>{product.productId}</td>
                                <td>{product.name}</td>
                                <td>{product.price}</td>
                                <td>{product.stockQuantity}</td>
                                <td>
                                    <button onClick={() => {
                                        setSelectedProduct(product);
                                        setProductForm({
                                            productId: product.productId,
                                            name: product.name,
                                            price: product.price,
                                            description: product.description,
                                            stockQuantity: product.stockQuantity,
                                        });
                                        fetchImages(product.productId);
                                    }}
                                    >
                                        Update
                                    </button>
                                    <button onClick={() => handleArchiveProduct(product.productId)}>Archive</button>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>

                    {selectedProduct && (
                        <div>
                            <h3>Update Product</h3>
                            <form onSubmit={(e) => e.preventDefault()}>
                                <label>
                                    Name:
                                    <input
                                        type="text"
                                        name="name"
                                        value={productForm.name}
                                        onChange={handleProductFormChange}
                                    />
                                </label>
                                <label>
                                    Price:
                                    <input
                                        type="number"
                                        name="price"
                                        value={productForm.price}
                                        onChange={handleProductFormChange}
                                    />
                                </label>
                                <label>
                                    Description:
                                    <textarea
                                        name="description"
                                        value={productForm.description}
                                        onChange={handleProductFormChange}
                                    />
                                </label>
                                <label>
                                    Stock Quantity:
                                    <input
                                        type="number"
                                        name="stockQuantity"
                                        value={productForm.stockQuantity}
                                        onChange={handleProductFormChange}
                                    />
                                </label>
                                <button onClick={() => handleUpdateProduct}>Save Changes</button>
                            </form>
                            <div>
                                <h3>Manage Images</h3>
                                <div>
                                    <h4>Existing Images</h4>
                                    {images.map((image, index) => (
                                        <div key={index}>
                                            <img src={`http://localhost:8888/assets/images/large/${selectedProduct.productId}/${image}`} alt={`Product ${index}`} width={100}/>
                                            <button onClick={() => removeImage(index, "existing")}>Remove</button>
                                        </div>
                                    ))}
                                </div>
                                <div>
                                    <h4>New Images</h4>
                                    {newImages.map((image, index) => (
                                        <div key={index}>
                                            <span>{image.name}</span>
                                            <button onClick={() => removeImage(index, "new")}>Remove</button>
                                        </div>
                                    ))}
                                </div>
                                <input type="file" multiple onChange={handleImageChange}/>
                                <button onClick={handleUploadImages}>Upload All Images</button>
                            </div>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
}

export default AdminDashboard;
