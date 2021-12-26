import './App.css';
import React, { Component } from 'react';


function Circle(props){
    return(
        <div style = {{borderRadius: '50%', width: 10, height: 10, backgroundColor: props.color, alignItems: 'center', position: 'relative', left: ((50 - props.pos) + '%'), display: 'table-cell'}}>
        </div>
    )
}


export default class PlayerCell extends Component {
    
    constructor(props) {
        super(props);
        this.state = { circleColors: ['white', 'black', 'purple'] };
        this.makeCircles = this.makeCircles.bind(this);
    }
    // render(){
    makeCircles(colorList){
        var circleList = [];
        for (let index = 0; index < colorList.length; index++) {
            circleList.push(<Circle color = {colorList[index]} pos = {(index - 1)*5} key = {index}></Circle>)
            // circleList.push(<div>test</div>)
        }
        return(
            <div style = {{display: 'table-row',}}>
                {circleList}
                {/* <div style = {{width: 10, height: 10, borderRadius: '50%', backgroundColor: 'black'}}></div> */}
                {/* test text */}
            </div>
        )
    }

    render() {
        const { circleColors } = this.state;
        return(
            <div style = {{ position: 'relative', top: '50%'}}>
                {this.makeCircles(circleColors)}
            </div>
        );
    }
}