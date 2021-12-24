// import logo from './logo.svg';
import './App.css';
import React, { Component } from 'react';
import background from "./monopoly-board.jpg";
import GoldenLayout, { Row, Stack, createGoldenLayoutComponent } from 'react-golden-layout';

export class Board extends Component {

  constructor(props) {
    super(props);
    this.state = { sideLength: Math.min(window.innerWidth, window.innerHeight) };
    this.handleResize = this.handleResize.bind(this);
  }

  handleResize(e) {
    console.log('handling')
    this.setState({ sideLength: Math.min(window.innerWidth, window.innerHeight) });
  };

  componentDidMount() {
    console.log('mounted')
    window.addEventListener("resize", this.handleResize);
  }

  componentWillUnmount() {
    window.addEventListener("resize", this.handleResize);
  }

  render() {
    const { sideLength } = this.state;
    return(
      <div className="App" style = {{width: sideLength, height: sideLength, alignItems: 'center', backgroundImage: `url(${background})`, backgroundSize: 'cover'}}>
        {/* <img src = {require('./monopoly-board.jpg')} alt = 'board' width = '100%' height = '100%'/> */}
        <div style = {{width: '100%', height: '100%'}}>
            <GoldenLayout>
                
            </GoldenLayout>
        </div>
      </div>
    )
  }
}
