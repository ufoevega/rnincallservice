
# react-native-incallservice

## Getting started

`$ npm install react-native-incallservice --save`

### Mostly automatic installation

`$ react-native link react-native-incallservice`

### Manual installation


#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNIncallservicePackage;` to the imports at the top of the file
  - Add `new RNIncallservicePackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-incallservice'
  	project(':react-native-incallservice').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-incallservice/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-incallservice')
  	```


## Usage
```javascript
import RNIncallservice from 'react-native-incallservice';

// TODO: What to do with the module?
RNIncallservice;
```
  