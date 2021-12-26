// import logo from './logo.svg';
import './App.css';
import React, { Component } from 'react';
import background from "./monopoly-board.jpg";
import PlayerCell from "./Cell.js";

//TODO: CREATE ARRAY OF CELLS

export class Board extends Component {

  constructor(props) {
    super(props);
    this.state = { sideLength: Math.min(window.innerWidth, window.innerHeight), propertyWidth: 0, columnWidth: 0, rowHeight: 0};
    this.handleResize = this.handleResize.bind(this);
    this.tophorizontalProperty = this.tophorizontalProperty.bind(this);
    this.bottomhorizontalProperty = this.bottomhorizontalProperty.bind(this);
    this.verticalProperty = this.verticalProperty.bind(this);
    this.state.columnWidth = this.state.sideLength*(214/1600);
    this.state.rowHeight = this.state.sideLength*(214/1600)
    this.state.propertyWidth = this.state.sideLength*(130.3/1600)
  }



  handleResize(e) {
    console.log('handling resize')
    this.setState({ sideLength: Math.min(window.innerWidth, window.innerHeight) });
    this.setState({ columnWidth: this.state.sideLength*(214/1600), rowHeight: this.state.sideLength*(214/1600), propertyWidth: this.state.sideLength*(130.3/1600)})
  };

  componentDidMount() {
    console.log('mounted')
    window.addEventListener("resize", this.handleResize);
  }

  componentWillUnmount() {
    window.addEventListener("resize", this.handleResize);
  }

  tophorizontalProperty(index, color) {
    return(
        <div style = {{width: this.state.propertyWidth, alignItems: 'center', height: this.state.rowHeight, position: 'relative', left: 0, display: 'table-cell'}}>
            <div style = {{width: this.state.propertyWidth, height: this.state.rowHeight*(162/214), backgroundColor: color}}>
                <PlayerCell/>
            </div>
            <div style = {{width: this.state.propertyWidth, height: this.state.rowHeight*(52/214)}}>
                {/* house cell */}
            </div>
        </div>
    )
  }

  bottomhorizontalProperty(index, color) {
    return(
        <div style = {{width: this.state.propertyWidth, alignItems: 'center', height: this.state.rowHeight, position: 'relative', left: 0, display: 'table-cell'}}>
            <div style = {{width: this.state.propertyWidth, height: this.state.rowHeight*(52/214)}}>
                {/* house cell */}
            </div>
            <div style = {{width: this.state.propertyWidth, height: this.state.rowHeight*(162/214), backgroundColor: color}}>
                <PlayerCell/>
            </div>
        </div>
    )
  }

  blankProperty() {
      return(
          <div style = {{width: this.state.propertyWidth, alignItems: 'center', height: this.state.propertyWidth, position: 'relative', display: 'table-cell'}}>
          </div>
      )
  }

  verticalProperty(indexL, indexR, colorL, colorR) {
    return(
        <div style = {{display: 'table-row', position: 'relative', top: 0}}>
            <div style = {{position: 'relative', display: 'table-cell',  alignItems: 'center'}}>
                <div style = {{display: 'table-row'}}>
                    <div style = {{width: this.state.columnWidth*(165/214), height: this.state.propertyWidth, display: 'table-cell',  backgroundColor: colorL}}>
                        <PlayerCell/>
                    </div>
                    <div style = {{width: this.state.columnWidth*(49/214), height: this.state.propertyWidth, display: 'table-cell'}}>
                        {/* house cell */}
                    </div>
                </div>
            </div>
            {this.blankProperty()}
            {this.blankProperty()}
            {this.blankProperty()}
            {this.blankProperty()}
            {this.blankProperty()}
            {this.blankProperty()}
            {this.blankProperty()}
            {this.blankProperty()}
            {this.blankProperty()}
            <div style = {{position: 'relative', display: 'table-cell',  alignItems: 'center'}}>
                <div style = {{display: 'table-row'}}>
                    <div style = {{width: this.state.columnWidth*(49/214), height: this.state.propertyWidth, display: 'table-cell'}}>
                        {/* house cell */}
                    </div>
                    <div style = {{width: this.state.columnWidth*(165/214), height: this.state.propertyWidth, display: 'table-cell',  backgroundColor: colorR}}>
                        <PlayerCell/>
                    </div>
                </div>
            </div>
        </div>
    )
  }

  render() {
    const { sideLength, rowHeight, columnWidth } = this.state;
    return(
      <div className="App" style = {{width: sideLength, height: sideLength, alignItems: 'center', backgroundImage: `url(${background})`, backgroundSize: 'cover', float: 'left'}}>
        {/* <img src = {require('./monopoly-board.jpg')} alt = 'board' width = '100%' height = '100%'/> */}
        <div style = {{display: 'table-row', position: 'relative', top: 0}}>
            <div style = {{width: columnWidth, height: rowHeight, position: 'relative', left: 0, display: 'table-cell', /*backgroundColor: 'goldenrod'*/}}>
                
            </div>
            {this.tophorizontalProperty('STRAN', 'green')}
            {this.tophorizontalProperty('CHANC', 'blue')}
            {this.tophorizontalProperty('FL ST', 'red')}
            {this.tophorizontalProperty('TR SQ', 'green')}
            {this.tophorizontalProperty('F STN', 'blue')}
            {this.tophorizontalProperty('LE SQ', 'red')}
            {this.tophorizontalProperty('CO ST', 'green')}
            {this.tophorizontalProperty('WTWRK', 'blue')}
            {this.tophorizontalProperty('PICDL', 'red')}
            <div style = {{width: columnWidth, height: rowHeight, position: 'relative', left: 0, display: 'table-cell', /*backgroundColor: 'goldenrod'*/}}>
                
            </div>
        </div>
        {this.verticalProperty('VI ST', 'RE ST', 'red', 'green')}
        {this.verticalProperty('ML ST', 'OX ST', 'blue', 'blue')}
        {this.verticalProperty('C C', 'C C', 'green', 'red')}
        {this.verticalProperty('BO ST', 'BN ST', 'red', 'green')}
        {this.verticalProperty('M STN', 'L STN', 'blue', 'blue')}
        {this.verticalProperty('NT AV', 'CHANC', 'green', 'red')}
        {this.verticalProperty('WHTHL', 'PK LN', 'red', 'green')}
        {this.verticalProperty('ELEC', 'SU TX', 'blue', 'blue')}
        {this.verticalProperty('PL ML', 'MAYFR', 'green', 'red')}
        <div style = {{display: 'table-row', position: 'relative', top: 0}}>
            <div style = {{width: columnWidth, height: rowHeight, position: 'relative', left: 0, display: 'table-cell', /*backgroundColor: 'goldenrod'*/}}>
                
            </div>
            {this.bottomhorizontalProperty('PT RD', 'red')}
            {this.bottomhorizontalProperty('EUSTN', 'blue')}
            {this.bottomhorizontalProperty('CHANC', 'green')}
            {this.bottomhorizontalProperty('ANGEL', 'red')}
            {this.bottomhorizontalProperty('K STN', 'blue')}
            {this.bottomhorizontalProperty('IN TX', 'green')}
            {this.bottomhorizontalProperty('WTCPL', 'red')}
            {this.bottomhorizontalProperty('C C', 'blue')}
            {this.bottomhorizontalProperty('OK RD', 'green')}
            <div style = {{width: columnWidth, height: rowHeight, position: 'relative', left: 0, display: 'table-PlayerCell', /*backgroundColor: 'goldenrod'*/}}>
                
            </div>
        </div>
      </div>
    )
  }
}
