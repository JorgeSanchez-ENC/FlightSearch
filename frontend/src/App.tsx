import React from 'react';
import logo from './logo.svg';
import './App.css';
import SearchForm from './components/SearchForm';
import { Route, Router, Routes,BrowserRouter } from 'react-router-dom';
import Results from './components/Results';
import { FlightResultProvider } from './contexts/FlightResultsContext';

function App() {
  return (
    <FlightResultProvider>
          <BrowserRouter>
              <Routes>
                  <Route path='/' element={<SearchForm/>}></Route>
                  <Route path='/results' element={<Results></Results>}></Route>
              </Routes>
        </BrowserRouter>

    </FlightResultProvider>


  );
}

export default App;
