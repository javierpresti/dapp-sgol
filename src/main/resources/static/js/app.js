function capitalizeFirst(string) {	return string.charAt(0).toUpperCase() + string.slice(1) }

function getString(data) { return 'get' + capitalizeFirst(data) }
function setString(data) { return 'set' + capitalizeFirst(data) }

function hasValue(value) { return typeof value !== 'undefined' }
function getElse(value, els) { return hasValue(value) ? value : els }

function passAttributes(objFrom, objTo) {
	for (var prop in objFrom) {
		if (objFrom.hasOwnProperty(prop)) {
			objTo[prop] = objFrom[prop];
		}
	}
	return objTo
}

function addGet(name, scope, http, subname) {
	var fullName = name + (hasValue(subname) ? capitalizeFirst(subname) : '')
	scope[getString(fullName)] = function(value) {
		http.get('/' + name + (hasValue(subname)?'/' + subname + 
				(hasValue(value) ? '/' + value : '') : '')).success(function(data) {
			scope[fullName] = data;
		})
	}
}

function addGetId(name, scope, http, subname) {
	var fullName = 'id' + (hasValue(subname) ? capitalizeFirst(subname) : '')
	scope[getString(fullName)] = function(id, valu) {
		http.get('/' + name + '/' + id + '/' + (hasValue(subname)?'/' + subname : '') + 
				(hasValue(valu)?'/'+valu:'')).success(function(data) {
			scope[fullName] = data;
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

function contr(name, subnames, alls, attributes, ids, subgets) {
	
	return function($scope, $http) {
		addGet(name, $scope, $http)		
		addPost(name, $scope, $http)
		subnames = getElse(subnames, [])
		subgets = getElse(subgets, [])
		ids = getElse(ids, [])
		subnames.forEach(function(subname) {addPost(name, $scope, $http, subname)})
		subgets.forEach(function(subget) {addGet(getElse(subget.name, name), $scope, $http, subget.subname)})
		ids.forEach(function(idName) {addGetId(name, $scope, $http, idName)})
		
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
			$scope.editing = passAttributes(item, {});
		}
		$scope.editItem = function() {
			obj = $scope.editing
			args = [].slice.apply(arguments)
			args.forEach(function(arg) {
				$scope[setString(arg)]($scope[arg], obj["id"]);
				delete $scope[arg];
			})
			$scope.editing = null
		}
		
		passAttributes(attributes, $scope)
		
		$scope.back = function() {
			$scope.editing = null
			$scope.viewing = null
		}
		
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
	.controller('teams', contr('teams', ['name','totalpoints','player','playerremove','captain'], [], {}, ['position']))
	.controller('players', contr('players', ['points','goals', 'all'], ['goals'], {position:'Defender'}))
	.controller('leagues', contr('leagues', ['round','team','teamremove','init'], [], {minTeams:2, maxTeams:2}, ['teamsToAdd']))
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
