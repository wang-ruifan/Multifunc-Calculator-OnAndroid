# Android Calculator

## For English Version, Please Click [Here](#english-version)

这是一个科学计算器应用程序，在竖屏模式下为普通计算器，横屏模式下为科学计算器。该应用程序支持对数、阶乘、三角函数、幂、根号等功能。同时提供历史记录，并可点击历史记录快速填充历史计算结果。

## 仓库说明
这是我的第二个 Android 项目，用于学习 Android 开发。该项目为大三安卓课程的附加可选大作业，最终成绩为 100 分。  
- 此仓库只作为个人学习记录，不提供任何形式的技术支持，不保证代码的正确性和稳定性。

## 功能特性

- **基础计算器功能**：
    - **普通计算器模式**：在竖屏模式下，提供基本的加、减、乘、除运算。
    - **科学计算器模式**：在横屏模式下，提供高级的科学计算功能，包括对数、阶乘、三角函数、幂、���号等。
    - **显示模式切换**：支持暗色模式和亮色模式的切换。
- **历史记录功能**：
    - **记录显示**：保存并显示所有计算历史，包括运算过程和结果。
    - **结果调用**：点击历史记录中的某一项，可将该条记录的输入或结果快速填充至当前输入。
    - **记录管理**：支持清空所有历史记录。
    - **持久化存储**：支持退出应用后自动保存最近的十条历史记录。
- **算账功能**：
    - **动态输入**：根据选择的人数动态生成对应数量的个人金额输入框。
    - **共同金额**：支持输入共同承担的金额，用于平均分配。
    - **个人金额**：支持输入每个人额外需要支付或已支付的金额。
    - **自动填充**：主计算器界面的计算结果可自动填充至算账功能中的共同金额输入框。
    - **账单计算**：一键计算每人最终需支付的金额。
    - **结果分享**：在账单结果页面提供分享按钮，方便快速分享账单详情。
- **汇率换算功能**：
    - **货币排序**：允许用户通过拖动滑块调整货币列表的显示顺序。
    - **结果填充**：主计算器界面的计算结果可自动填充至汇率换算中的第一个货币金额输入框。
    - **历史调用**：支持从历史记录中选择结果，并填充至当前光标所在的货币金额输入框。
    - **实时汇率**：通过API实时获取最新的货币汇率。

## 安装

### 从源代码构建  

1. 克隆此仓库到本地：
    ```bash
    git clone https://github.com/yourusername/Android-calculator.git
    ```
2. 使用 Android Studio 打开项目。
3. 非常关键！ 安装jdk11并设置编译gradle jdk为11
4. 连接你的 Android 设备或启动模拟器。
5. 点击运行按钮将应用程序安装到设备上。

### 下载APK文件  

你也可以直接下载 APK 文件并安装到你的 Android 设备上。  
请到[发布页](https://github.com/wang-ruifan/Multifunc-Calculator-OnAndroid/releases)下载所需版本。

## 使用说明

1.  启动应用程序。
2.  **基础计算**：
    *   在竖屏模式下使用普通计算器功能。
    *   将设备旋转到横屏模式以使用科学计算器功能。
    *   进行计算后，结果将自动保存到历史记录中。
3.  **历史记录**：
    *   在主计算器界面，通常可以通过特定按钮或菜单访问历史记录列表。
    *   点击历史记录中的任意一项，可以选择将该条记录的算式或结果填充到当前输入框。
    *   可清空所有历史记录。最近的十条记录会在应用退出后保留。
4.  **支持黑暗模式**
    *   跟随系统的黑暗模式设置。
5.  **算账功能**：
    *   通过导航菜单中的算账按钮进入算账功能模块。
    *   选择参与分账的人数，应用将动态显示对应数量的金额输入框。
    *   在“共同金额”输入框中输入需要所有人平均分摊的总金额（主计算器界面的计算结果可自动填充至此）。
    *   在每个人对应的输入框内输入该人需要额外支付的金额。
    *   比如A,B,C三个人吃饭，点了两个菜三个人一起吃，这两个菜加起来150元。而A,B,C三个人分别点了一个主食，A点的主食50元，B点的主食30元，C点的主食70元。  
        那么在“共同金额”输入框中输入150元，在A的输入框中输入50元，在B的输入框中输入30元，在C的输入框中输入70元。
    *   点击计算按钮（位于右下角）查看每人最终需要支付的金额。
    *   在账单结果界面，可以点击右上角的分享按钮分享账单详情。
6.  **汇率换算功能**：
    *   通过导航菜单或主界面按钮进入汇率换算模块。
    *   主计算器界面的计算结果可以自动填充到汇率换算列表中的第一个货币金额输入框。
    *   点击某个货币的金额输入框，可以通过历史记录调用功能，将历史计算结果填充至此。
    *   通过拖动每个货币条目旁的滑块可以调整其在列表中的显示顺序。
    *   输入一个货币的金额后，其他货币的对应金额会根据实时汇率自动计算并显示。

## English Version

This is a scientific calculator application. In portrait mode, it functions as a standard calculator, and in landscape mode, it functions as a scientific calculator. The application supports logarithms, factorials, trigonometric functions, powers, roots, and more. It also provides a history feature, allowing users to quickly fill in past calculation results by clicking on the history entries.

## Repository Description
This is my second Android project, created to learn Android development. It was an optional extra assignment for my junior year Android course, and I received a final grade of 100.  
This repository is for personal learning purposes only and does not provide any form of technical support.

## Features

- **Basic Calculator Functions**:
    - **Standard Calculator Mode**: In portrait mode, it provides basic addition, subtraction, multiplication, and division operations.
    - **Scientific Calculator Mode**: In landscape mode, it offers advanced scientific calculation functions, including logarithms, factorials, trigonometric functions, powers, roots, and more.
    - **Display Mode Switching**: Supports switching between dark mode and light mode.
- **History Function**:
    - **Record Display**: Saves and displays all calculation history, including the calculation process and results.
    - **Result Invocation**: Clicking an item in the history can quickly fill its input or result into the current input.
    - **Record Management**: Supports clearing all history records.
    - **Persistent Storage**: Supports automatically saving the last ten history records after exiting the application.
- **Bill Splitting Function**:
    - **Dynamic Inputs**: Dynamically generates individual amount input fields based on the selected number of people.
    - **Shared Amount**: Supports inputting a common amount to be split equally.
    - **Individual Amounts**: Supports inputting amounts each person needs to pay or has paid individually.
    - **Auto-fill from Calculator**: The result from the main calculator interface can be automatically filled into the shared amount field in the bill splitter.
    - **Bill Calculation**: One-click calculation of the final amount each person needs to pay.
    - **Result Sharing**: Provides a share button on the bill results page for easy sharing of bill details.
- **Currency Conversion Function**:
    - **Currency Ordering**: Allows users to adjust the display order of currencies by dragging sliders.
    - **Result Auto-fill**: The result from the main calculator interface can be automatically filled into the first currency's amount input field.
    - **History Invocation**: Supports selecting results from history and filling them into the currency amount input field where the cursor is currently located.
    - **Real-time Exchange Rates**: Fetches the latest currency exchange rates in real-time via an API.

## Installation

### Build from Source

1. Clone this repository to your local machine:
    ```bash
    git clone https://github.com/yourusername/Android-calculator.git
    ```
2. Open the project with Android Studio.
3. Very important! Install JDK 11 and set the Gradle JDK for compilation to 11.
4. Connect your Android device or start an emulator.
5. Click the run button to install the application on your device.

### Download APK File

You can also download the APK file and install it on your Android device directly.  
Please visit the [release page](https://github.com/wang-ruifan/Multifunc-Calculator-OnAndroid/releases) to download the desired version.

## Usage Instructions

1.  Launch the application.
2.  **Basic Calculation**:
    *   Use the standard calculator functions in portrait mode.
    *   Rotate the device to landscape mode to use the scientific calculator functions.
    *   After performing calculations, the results will be automatically saved to the history.
3.  **History**:
    *   Access the history list, typically via a dedicated button or menu on the main calculator screen.
    *   Click any item in the history to choose whether to fill the expression or the result into the current input field.
    *   You can clear all history records. The last ten records will be saved even after exiting the app.
4.  **Dark Mode Support**
    *   Follows the system's dark mode settings.
5.  **Bill Splitting Function**:
    *   Navigate to the bill splitting module via the bill splitting button in the navigation menu.
    *   Select the number of people involved in splitting the bill; the app will dynamically show the corresponding number of amount input fields.
    *   Enter the total amount to be shared equally by everyone in the "Shared Amount" input field (the result from the main calculator can be auto-filled here).
    *   In each person's corresponding input field, enter the additional amount that person needs to pay.
    *   For example, if A, B, and C are dining together, they ordered two dishes to share which cost 150 yuan in total. A, B, and C also individually ordered main courses: A's main course was 50 yuan, B's was 30 yuan, and C's was 70 yuan.
        In this case, enter 150 yuan into the "Shared Amount" input field, 50 yuan into A's input field, 30 yuan into B's input field, and 70 yuan into C's input field.
    *   Click the calculate button (often found at the bottom right) to see the final amount each person needs to pay.
    *   On the bill results page, you can click the share button (usually in the top right corner) to share the bill details.
6.  **Currency Conversion Function**:
    *   Access the currency conversion module through the navigation menu or a button on the main screen.
    *   The result from the main calculator interface can be automatically filled into the amount input field for the first currency in the list.
    *   Click on an amount input field for a specific currency; you can then use the history invocation feature to fill a past calculation result into this field.
    *   Adjust the display order of currencies in the list by dragging the slider (or a similar control) next to each currency item.
    *   After entering an amount for one currency, the corresponding amounts for other currencies will be automatically calculated and displayed based on real-time exchange rates.
