// import logo from './logo.svg';
import './App.css';
import React from 'react';
import { Board } from './Board.js';

export default function App() {
  return (
    <div style={{justifyContent: 'center', alignItems: 'center', justifyItems: 'center', alignContent: 'center', display: 'flex'}}>
      <Board/>
    </div>
  )
}
