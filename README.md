# QuickBox

便捷末影箱 & 工作台指令插件，省去找末影箱和合成台的麻烦。

**支持平台**: Luminol / Folia / Paper 1.21.x

## 功能

- `/ec` 打开自己的末影箱（显示玩家名标题）
- `/wb` 打开便携工作台

## 指令

| 指令 | 别名 | 说明 | 权限 |
|------|------|------|------|
| `/enderchest` | `/ec` | 打开末影箱 | `quickbox.enderchest` (默认 OP) |
| `/workbench` | `/wb`, `/craft` | 打开工作台 | `quickbox.workbench` (默认 OP) |

## 构建

使用 Maven：

```bash
mvn clean package
```

或手动编译：

```bash
javac -cp "<paper-api-jar>" -d target/classes src/main/java/com/example/quickbox/QuickBox.java
cp src/main/resources/plugin.yml target/classes/
jar cfM QuickBox.jar -C target/classes .
```

## 许可证

WTFPL
