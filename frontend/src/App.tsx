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
import {AppUser} from "./types/AppUser.ts";


function App() {
    const [organizations, setOrganizations] = useState<Organization[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [totalPages, setTotalPages] = useState<number>(0);
    const [searchText, setSearchText] = useState<string>("");
    const [appUser, setAppUser] = useState<AppUser | null | undefined>(undefined);
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

    useEffect(() => {
        if (!appUser) {
            loadMe();
        }
    }, [appUser]);

    function loadMe() {
        axios.get("/api/users/me")
            .then(r => {
                console.log("API response:", r.data);
                setAppUser(r.data);
            })
            .catch(e => console.error(e))
    }

    function logout() {
        axios.post("/api/users/logout")
            .then(() => console.log("Logged out"))
            .catch(e => console.log(e))
            .finally(()=>setAppUser(null))
    }

    function login(username: string, password: string) {
        axios.post("/api/users/login", {}, {
            auth: {
                username: username,
                password: password
            }
        })
            .then(() => {
                loadMe()
            })
            .catch(e => {
                setAppUser(null)
                console.error(e)
            })
    }

    const handleLogin = (user: AppUser) => {
        setAppUser(user);
    };

    return (

        <div className="app-container">
            <Header appUser={appUser} logout={logout}/>

            <Routes>
                <Route
                    path="/"
                    element={<Home organizations={organizations} loading={loading} loadOrganizations={loadOrganizations}
                                   totalPages={totalPages} searchText={searchText} setSearchText={setSearchText}
                    appUser={appUser}/>}
                />
                <Route path="/add-organization" element={<OrganizationForm/>}/>
                <Route path="/edit-organization/:id" element={<OrganizationForm/>}/>
                <Route path="/organizations/:id" element={<OrganizationDetails appUser={appUser} onLogin={handleLogin} />}/>
                <Route path="/add-review/:id" element={<ReviewForm/>}/>
                <Route path="/login" element={<LoginPage login={login}/>}/>
                <Route path="/register" element={<RegisterPage/>}/>
            </Routes>
        </div>
    );
}

export default App;