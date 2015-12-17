function capitalizeFirst(string) {	return string.charAt(0).toUpperCase() + string.slice(1) }

function getString(data) { return 'get' + capitalizeFirst(data) }
function setString(data) { return 'set' + capitalizeFirst(data) }

function getElse(value, els) { return typeof value !== 'undefined' ? value : els; }

function passAttributes(objFrom, objTo) {
	for (var prop in objFrom) {
		if (objFrom.hasOwnProperty(prop)) {
			objTo[prop] = objFrom[prop];
		}
	}
}

function addGet(name, scope, http) {
	scope[getString(name)] = function() {
		http.get('/' + name).success(function(data) {
			scope[name] = data;
		})
	}
}

function addGetSub(name, scope, http, subname) {
	scope[getString(name + capitalizeFirst(subname))] = function(value) {
		http.get('/' + name + '/' + subname + '/' + value).success(function(data) {
			scope[name + capitalizeFirst(subname)] = data;
		})
	}
}

function addPost(name, scope, http, subname) {
	scope[setString(subname ? subname : name)] = function(value, id) {
		http.post('/' + name + '/' + (id?id+'/':'') + (subname?subname:''), value).success(function(data) {
			scope[getString(name)]()
		});
	}
}

function contr(name, subnames, alls, attributes, subgets) {
	
	return function($scope, $http) {
		addGet(name, $scope, $http)
		addPost(name, $scope, $http)
		subnames = getElse(subnames, [])
		subgets = getElse(subgets, [])
		subnames.forEach(function(subname) {addPost(name, $scope, $http, subname)})
		subgets.forEach(function(subget) {addGetSub(getElse(subget.name, name), $scope, $http, subget.subname)})
		
		getElse(alls, []).forEach(function(all) {
			$scope[setString(all)+'All'] = function(list) {
				list.forEach(function(item) {
					$scope[setString(all)](new Number(item[all]), item.id)
				})
			}
		})
		
		
		$scope.viewing = null
		$scope.setViewItem = function(item) {
			$scope.viewing = item;
		}

		$scope.editing = null
		$scope.setEditItem = function(item) {
			$scope.editing = item;
		}
		$scope.editItem = function() {
			obj = $scope.editing
			args = [].slice.apply(arguments)
			args.forEach(function(arg) { $scope[setString(arg)](obj[arg], obj["id"]) })
			$scope.editing = null
		}		
		
		passAttributes(attributes, $scope)
				
		$scope[getString(name)]()
	}
}

function route(routeProvider, name, controller) {
		routeProvider.when('/' + name, {
		controller: getElse(controller, name),
		templateUrl: '/js/' + name + '.html'
	});
}

var app = angular.module('app', ['ngRoute'])
	.controller('teams', contr('teams', ['name','totalpoints','player','playerremove','captain'], [], {}, [{name:'players',subname:'position'}]))
	.controller('players', contr('players', ['points','goals', 'all'], ['goals'], {position:'Defender'}))
	.controller('leagues', contr('leagues', ['round','team','init'], [], {minTeams:2, maxTeams:2}))
	.controller('rounds', contr('players', ['match']))
	.controller('matches', contr('matches', ['points']))
;

app.config(function ($routeProvider, $httpProvider) {
	route($routeProvider, 'players')
	route($routeProvider, 'teams')
	route($routeProvider, 'ranking', 'teams')
	route($routeProvider, 'round', 'players')
	route($routeProvider, 'leagues')

	$routeProvider.otherwise({ redirectTo: "/players" });
	
	$httpProvider.defaults.headers.common["X-Requested-With"] = 'XMLHttpRequest';
});
