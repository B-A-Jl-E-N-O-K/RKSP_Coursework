import logo from './logo.svg';
import './App.css';


class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {securities: []};
	}

	componentDidMount() {
		client({method: 'GET', path: '/seclist'}).done(response => {
			this.setState({securities: response.entity._embedded.securities});
		});
	}

	render() {
		return (
			<SecuritiesList securities={this.state.securities}/>
		)
	}
}

class SecuritiesList extends React.Component{
	render() {
		const securities = this.props.securities.map(security =>
			<Security security={security}/>
		);
		return (
			<table>
				<tbody>
          <tr>
            <th>Id</th>
            <th>strId</th>
            <th>supId</th>
            <th>Name</th>
          </tr>
					{securities}
				</tbody>
			</table>
		)
	}
}

class Security extends React.Component{
	render() {
		return (
			<tr>
				<td>{this.props.security.id}</td>
				<td>{this.props.security.strId}</td>
				<td>{this.props.security.supId}</td>
        <td>{this.props.security.name}</td>
			</tr>
		)
	}
}



export default App;
