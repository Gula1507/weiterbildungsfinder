import './App.css'
import {Route, Routes} from "react-router-dom";
import {useEffect, useState} from "react";
import axios from "axios";
import {Organization} from "./types/Organization.ts";
import Home from "./components/Home.tsx";
import OrganizationForm from "./components/OrganizationForm.tsx";
import OrganizationDetails from "./components/OrganizationDetails.tsx";
import Header from "./components/Header.tsx";
import ReviewForm from "./components/ReviewForm.tsx";
import LoginPage from "./components/LoginPage.tsx";
import RegisterPage from "./components/RegisterPage.tsx";


function App() {
    const [organizations, setOrganizations] = useState<Organization[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [searchText, setSearchText] = useState<string>("");

    const loadOrganizations = (page: number, size: number, searchText: string) => {
        setLoading(true);
        axios.get(`/api/organizations?page=${page}&size=${size}&search=${searchText}`)
            .then((response) => {
                setOrganizations(response.data.content);
                setTotalPages(response.data.totalPages);
                setLoading(false);
            })
            .catch((error) => {
                console.error(error);
                setLoading(false);
            });
    };

    useEffect(() => {
        loadOrganizations(0, 10, searchText);
    }, [searchText]);

    function logout() {
        axios.post("api/users/logout")
            .then(()=>console.log("Logged out"))
            .catch(e=>console.log(e))
    }
    return (
        <div className="app-container">
            <Header/>
            <button onClick={logout}>Logout</button>
            <Routes>
                <Route
                    path="/"
                    element={<Home organizations={organizations} loading={loading} loadOrganizations={loadOrganizations}
                                   totalPages={totalPages} searchText={searchText} setSearchText={setSearchText}/>}
                />
                <Route path="/add-organization" element={<OrganizationForm/>}/>
                <Route path="/edit-organization/:id" element={<OrganizationForm/>}/>
                <Route path="/organizations/:id" element={<OrganizationDetails/>}/>
                <Route path="/add-review/:id" element={<ReviewForm/>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
            </Routes>
        </div>
    );
}

export default App;